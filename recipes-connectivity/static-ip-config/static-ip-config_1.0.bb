# -------------------
# Configures the system to use a static IP
# -------------------

# Depends on the following custom env vars exported to the yocto build:
# - NETWORK_STATIC_IP

SUMMARY = "Configures the system to use a static IP."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI += " \
    file://eth.network \
    file://en.network \
    file://wlan.network \
"

FILES_${PN} += " \
    ${sysconfdir}/systemd/network/* \
"

RDEPENDS_${PN} += "systemd"

# Use the preplace class to patch template source files
inherit preplace
inherit logging

TEMPLATE_FILES = "\
    ${WORKDIR}/en.network \
    ${WORKDIR}/eth.network \
    ${WORKDIR}/wlan.network \
"

python do_patch() {
    static_ip =  d.getVar('NETWORK_STATIC_IP', True)
    if static_ip is None:
        bb.error "No static IP configured, but package 'system-static-ip' included!"

    params = {
        "NETWORK_STATIC_IP": static_ip
    }
    files = d.getVar('TEMPLATE_FILES', True).split()
    for file in files:
        preplace_execute(file, params)
}

do_install() {
    install -d ${D}${sysconfdir}/systemd/network
    install -m 0644 ${WORKDIR}/eth.network ${D}${sysconfdir}/systemd/network
    install -m 0644 ${WORKDIR}/en.network ${D}${sysconfdir}/systemd/network
    install -m 0644 ${WORKDIR}/wlan.network ${D}${sysconfdir}/systemd/network
}