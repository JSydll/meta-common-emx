# -------------------
# Configures the WPA client for wifi connections
# -------------------

# Depends on the following custom env vars exported to the yocto build:
# - WIFI_SSID
# - WIFI_PWD

SUMMARY = "Configures the WPA client for wifi connections."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI += "file://wifi-ap.conf.j2"

DISTRO_FEATURES_append = "wifi"

# Ensure the wpa-supplicant is available 
RDEPENDS_${PN} += "systemd wpa-supplicant"

inherit systemd

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_${PN} += "wifi-ap.service"

inherit templating
require create_wpa_psk.inc

TEMPLATE_FILE = "${WORKDIR}/wifi-ap.conf.j2"

python do_patch_append() {
    ssid = d.getVar('WIFI_SSID', True)
    pwd = d.getVar('WIFI_PWD', True)
    params = { 
        "ssid": ssid,
        "psk": create_wpa_psk(ssid, pwd)
    }
    render_template(d.getVar('TEMPLATE_FILE', True), params)
}

do_install_append () {
    install -d ${D}${sysconfdir}/wpa_supplicant/
    install -D -m 600 ${WORKDIR}/wifi-ap.conf ${D}${sysconfdir}/wpa_supplicant/

    install -d ${D}${sysconfdir}/systemd/system/multi-user.target.wants/
    ln -s ${systemd_unitdir}/system/wpa_supplicant@.service ${D}${sysconfdir}/systemd/system/multi-user.target.wants/wifi-ap.service
}