# Explicitly make sure, networking is managed by systemd
PACKAGECONFIG_append = " networkd resolved"