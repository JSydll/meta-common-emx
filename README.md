# meta-common-configs

Collection of commonly used configurations for a (head-less) linux system.

The features can be configured using environment variables (see below).


## Usage & dependencies

Depends on `poky` and `meta-poky-extensions`. 

To integrate, add to your `bblayers.conf` and whitelist the environment variables
by sourcing the `scripts/whitelisting.sh` script.

If a variable is left empty, the corresponding feature may not take effect.

An example for possible variable assignments can be found in `.env.example`.


## Packages and classes

**Users and usage**:

- `set-root-pwd` class (variables: `ROOT_PWD`):
  - Sets a root password. Must be inherited by an image recipe.
- `loadkeys` (variables: `KEYBOARD_PROFILE`):
  - Loading a certain keyboard profile.

**Connectivity**:

- `static-ip-config` (variables: `NETWORK_STATIC_IP`):
  - Setting a static IP.
- `wifi-ap-config` (variables: `WIFI_SSID`, `WIFI_PWD`):
  - Configuring WIFI accesss.

- Configuring an `openssh` server (e.g. permitting root access).
  Needs the `openssh` package to be included.


## Contribution

Feel free to contact me in case you have feature proposals or want to contribute.
