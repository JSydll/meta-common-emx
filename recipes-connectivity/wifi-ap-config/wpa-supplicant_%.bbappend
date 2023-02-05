# -------------------
# Configures the WPA client for wifi connections
# -------------------

# Depends on the following custom env vars exported to the yocto build:
# - WIFI_SSID
# - WIFI_PWD
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://wifi-ap.conf.j2"

DISTRO_FEATURES:append = "wifi"

inherit templating
require create_wpa_psk.inc

TEMPLATE_FILE = "${WORKDIR}/wifi-ap.conf.j2"

# Alias following a more feature-oriented naming
RPROVIDES:${PN} = "wifi-ap-config"

python do_patch:append() {
    ssid = d.getVar('WIFI_SSID', True)
    pwd = d.getVar('WIFI_PWD', True)
    params = { 
        "ssid": ssid,
        "psk": create_wpa_psk(ssid, pwd)
    }
    render_template(d.getVar('TEMPLATE_FILE', True), params)
}

do_install:append () {
    install -d ${D}${sysconfdir}
    install -D -m 600 ${WORKDIR}/wifi-ap.conf ${D}${sysconfdir}/wpa_supplicant.conf
}