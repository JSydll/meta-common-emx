# -------------------
# Configuration of openssh
# -------------------

# Depends on the following custom env vars exported to the yocto build:
# - SSH_ALLOW_ROOT

do_install:append() {
    if [ "${SSH_ALLOW_ROOT}" = "1" ]; then
        sed -i 's|^#\(PermitRootLogin\).*|\1 yes|' ${D}${sysconfdir}/ssh/sshd_config
    fi
}