# -------------------
# Configures the WPA client for wifi connections
# -------------------

# Depends on the following custom env vars exported to the yocto build:
# - WIFI_SSID
# - WIFI_PWD

# Configures wifi (with nl-802.11 standard)

SRC_URI += "file://wifi-ap.conf"

DISTRO_FEATURES_append = "wifi"

# Ensure the wpa-supplicant is available 
RDEPENDS_${PN} += "systemd wpa-supplicant"

inherit systemd

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_${PN} += "wifi-ap.service"

# Use the preplace class to patch template source files
inherit preplace

TEMPLATE_FILE = "${WORKDIR}/wifi-ap.conf"

require create_wpa_psk.inc

python do_patch_append() {
    ssid = d.getVar('WIFI_SSID', True)
    pwd = d.getVar('WIFI_PWD', True)
    params = { 
        "WIFI_SSID": ssid,
        "WIFI_PSK": create_wpa_psk(ssid, pwd)
    }
    preplace_execute(d.getVar('TEMPLATE_FILE', True), params)
}

do_install_append () {
    install -d ${D}${sysconfdir}/wpa_supplicant/
    install -D -m 600 ${WORKDIR}/wifi-ap.conf ${D}${sysconfdir}/wpa_supplicant/

    install -d ${D}${sysconfdir}/systemd/system/multi-user.target.wants/
    ln -s ${systemd_unitdir}/system/wpa_supplicant@.service ${D}${sysconfdir}/systemd/system/multi-user.target.wants/wifi-ap.service
}