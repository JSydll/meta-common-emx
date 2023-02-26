# -------------------
# Configures the WPA client for wifi connections
# -------------------

# Depends on the following custom env vars exported to the yocto build:
# - WIFI_SSID
# - WIFI_PWD
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://wpa_supplicant-wlan0.conf.j2"

inherit templating
require create_wpa_psk.inc

TEMPLATE_FILE = "${WORKDIR}/wpa_supplicant-wlan0.conf.j2"

python do_patch:append() {
    ssid = d.getVar('WIFI_SSID', True)
    pwd = d.getVar('WIFI_PWD', True)
    params = { 
        "ssid": ssid,
        "psk": create_wpa_psk(ssid, pwd)
    }
    render_template(d.getVar('TEMPLATE_FILE', True), params)
}

# Enable the service on boot
SYSTEMD_SERVICE:${PN}:append = " wpa_supplicant@wlan0.service "
SYSTEMD_AUTO_ENABLE = "enable"

do_install:append () {
    install -d ${D}${sysconfdir}/wpa_supplicant/
    install -D -m 600 ${WORKDIR}/wpa_supplicant-wlan0.conf ${D}${sysconfdir}/wpa_supplicant/wpa_supplicant-wlan0.conf
}