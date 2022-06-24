# -------------------
# Installs a systemd service that loads a certain keyboard layout
# -------------------

# Depends on the following custom env vars exported to the yocto build:
# - KEYBOARD_PROFILE

DESCRIPTION = "Setup a specific keyboard-layout for the console"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://loadkeys.service \
"

inherit systemd

FILES_${PN} += "${sysconfdir}/systemd"

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_${PN} += "loadkeys.service"

# Use the preplace class to patch template source files
inherit preplace

TEMPLATE_FILE = "${WORKDIR}/loadkeys.service"

python do_patch_append() {
    profile = d.getVar('KEYBOARD_PROFILE', True)
    if profile is None:
        bb.error "No keyboard profile specified, but package 'loadkeys' included!"
    
    params = {
        "PROFILE" : profile 
    }
    preplace_execute(d.getVar('TEMPLATE_FILE', True), params)
}

do_install () {
    install -d ${D}${sysconfdir}/systemd/system
    install -D -m 0644 ${WORKDIR}/loadkeys.service ${D}${sysconfdir}/systemd/system/

    install -d ${D}${sysconfdir}/systemd/system/multi-user.target.wants/
    ln -s ${systemd_unitdir}/system/loadkeys.service ${D}${sysconfdir}/systemd/system/multi-user.target.wants/loadkeys.service
}