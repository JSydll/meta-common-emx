# -------------------
# Sets up the system to automatically mount media devices
# -------------------

# Mostly taken from https://serverfault.com/questions/766506/automount-usb-drives-with-systemd/767079.

SUMMARY = "Sets up the system to automatically mount media devices"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI += " \
    file://sbin/mount-media-device.sh \
    file://systemd/media-automount@.service \
    file://udev/99-media-automount.rules \
"

RDEPENDS:${PN} += "systemd udev bash"

inherit systemd

FILES:${PN} += " \
    ${sbindir}/* \
    ${sysconfdir}/udev/rules.d/* \
    ${systemd_unitdir}/system/* \
"

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE:${PN} += "media-automount@.service"

do_install () {
    install -d ${D}${sbindir}
    install -d ${D}${systemd_unitdir}/system
    install -d ${D}${sysconfdir}/udev/rules.d

    install -m 0775 ${WORKDIR}/sbin/mount-media-device.sh ${D}${sbindir}/mount-media-device
    install -m 0644 ${WORKDIR}/systemd/media-automount@.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${WORKDIR}/udev/99-media-automount.rules ${D}${sysconfdir}/udev/rules.d/
}