# -------------------
# Configures the root user
# -------------------
#
# Must be inherited by an image recipe (as user operations have image scope).

# Depends on the following custom env vars exported to the yocto build:
# - ROOT_PWD

inherit extrausers

# Note: The single quotes are necessary to avoid further variable expansion
EXTRA_USERS_PARAMS += "\
    usermod --password '$(openssl passwd -6 ${ROOT_PWD})' root; \
"