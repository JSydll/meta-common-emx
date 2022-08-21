# -------------------
# Configures the system to use a static IP
# -------------------

# Depends on the following custom env vars exported to the yocto build:
# - NETWORK_STATIC_IP

SUMMARY = "Configures the system to use a static IP."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI += " \
    file://static.network.j2 \
"

FILES_${PN} += " \
    ${sysconfdir}/systemd/network/* \
"

RDEPENDS_${PN} += "systemd"

inherit logging
inherit templating

python do_patch() {
    static_ip = d.getVar('NETWORK_STATIC_IP', True)
    if static_ip is None:
        bb.error("No static IP configured, but package 'system-static-ip' included!")
    work_dir = d.getVar('WORKDIR', True)
    template_path = ("%s/static.network.j2" % work_dir)

    # Treat the last portion of the IP as "extension" and split it off to determine gateway IP
    network_base, participant = os.path.splitext(static_ip)
    gateway_ip = ("%s.1" % network_base)
    
    params = {
        "interface_type": "",
        "static_ip": static_ip,
        "gateway_ip": gateway_ip
    }

    params["interface_type"] = "en"
    render_template(template_path, params, "en.network")
    params["interface_type"] = "eth"
    render_template(template_path, params, "eth.network")
    params["interface_type"] = "wlan"
    render_template(template_path, params, "wlan.network")
}

do_install() {
    install -d ${D}${sysconfdir}/systemd/network
    install -m 0644 ${WORKDIR}/eth.network ${D}${sysconfdir}/systemd/network
    install -m 0644 ${WORKDIR}/en.network ${D}${sysconfdir}/systemd/network
    install -m 0644 ${WORKDIR}/wlan.network ${D}${sysconfdir}/systemd/network
}