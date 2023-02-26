# -------------------
# Configures u-boot
# -------------------

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://no_boot_delay.cfg"