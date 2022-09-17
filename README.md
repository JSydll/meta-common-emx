# meta-common-emx

Collection of commonly used configurations and features for a (head-less) linux system.

The features can be configured using environment variables (see below).


## Usage & dependencies

Depends on `poky` and `meta-poky-extensions`. 

To integrate, add to your `bblayers.conf` and whitelist the environment variables
by sourcing the `scripts/whitelisting.sh` script.

If a variable is left empty, the corresponding feature may not take effect.

An example for possible variable assignments can be found in `.env.example`.


## Packages and classes

**Users and usage**:

- `set-root-pwd` class:
  - Sets a root password (variables: `ROOT_PWD`). Must be inherited by an image recipe.
- `loadkeys` package:
  - Allows to load a certain keyboard profile (variables: `KEYBOARD_PROFILE`).

**Connectivity**:

- `static-ip-config` package:
  - Sets a static IP (variables: `NETWORK_STATIC_IP`).
- `wifi-ap-config` package:
  - Configures WIFI accesss (variables: `WIFI_SSID`, `WIFI_PWD`).

- Configuring an `openssh` server (e.g. permitting root access).
  Needs the `openssh` package to be included.

**Media**:

- `media-automount` package:
  - Adds automount capabilities for external devices (e.g. USB).

## Contribution

Feel free to contact me in case you have feature proposals or want to contribute.
