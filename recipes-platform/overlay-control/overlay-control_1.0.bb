# -------------------
# Provides a CLI to control overlays
# -------------------

SUMMARY = "Provides a CLI to control overlays (e.g. to clear an overlay)."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI += " \
    file://usr/bin/overlay-control.sh \
"

RDEPENDS:${PN} = "bash"

do_install () {
    install -d ${D}${bindir}
    install -m 0775 ${WORKDIR}/usr/bin/overlay-control.sh ${D}${bindir}/overlay-control
}