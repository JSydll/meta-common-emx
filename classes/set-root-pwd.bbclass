# -------------------
# Configures the root user
# -------------------
#
# Must be inherited by an image recipe (as user operations have image scope).

# Depends on the following custom env vars exported to the yocto build:
# - ROOT_PWD

inherit extrausers

ROOT_PWD_ENCRYPTED = "$(mkpasswd -m sha256crypt ${ROOT_PWD})"

EXTRA_USERS_PARAMS += "\
    usermod --password '${ROOT_PWD_ENCRYPTED}' root; \
"