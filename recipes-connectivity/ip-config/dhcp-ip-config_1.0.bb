# -------------------
# Configures the system to use dynamic IPs
# -------------------

SUMMARY = "Configures the system to use a dynamic IP as assigned by the DHCP server."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI += " \
    file://dhcp.network.j2 \
"

FILES_${PN} += " \
    ${sysconfdir}/systemd/network/* \
"

RDEPENDS_${PN} += "systemd"

inherit templating

python do_patch() {
    work_dir = d.getVar('WORKDIR', True)
    template_path = ("%s/dhcp.network.j2" % work_dir)

    params = {
        "interface_type": ""
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