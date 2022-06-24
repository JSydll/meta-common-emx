# -------------------
# Adds additional base file configurations
# -------------------

# Depends on the following custom env vars exported to the yocto build:
# - HOST_NAME
# - CUSTOM_DISTRO_NAME
# - CUSTOM_DISTRO_VERSION

# -------------------
# Overwrite the hostname variable
# -------------------
do_install_prepend() {
    if [ "${HOST_NAME}" ]; then
        hostname="${HOST_NAME}"
    fi
}

CUSTOM_DISTRO_NAME ?= "${DISTRO_NAME}"
CUSTOM_DISTRO_VERSION ?= "${DISTRO_VERSION}"

# -------------------
# Overwrites the /etc/issue file with custom distro info
# ------------------
do_install_append() {
    printf "%s " "${CUSTOM_DISTRO_NAME}" > ${D}${sysconfdir}/issue
    printf "%s " "${CUSTOM_DISTRO_VERSION}" >> ${D}${sysconfdir}/issue
    printf "\\\n \\\l\n" >> ${D}${sysconfdir}/issue
	echo >> ${D}${sysconfdir}/issue
}