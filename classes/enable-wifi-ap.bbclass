# -------------------
# Sets up wifi for the inheriting image
# -------------------
#
# Must be inherited by an image recipe (as distro settings have image scope).

DISTRO_FEATURES:append = " wifi "

IMAGE_INSTALL += " \
    wpa-supplicant \
"

# Note: For correct hardwar functionality, certain linux firmware might be required
# in addition to the generic packages added here.