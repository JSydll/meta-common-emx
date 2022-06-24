# -------------------
# Creates a WPA PSK key from an SSID and a password
# -------------------
def create_wpa_psk(ssid, password):
    import hashlib, binascii
    binkey = hashlib.pbkdf2_hmac(
                'sha1',
                str.encode(password),
                str.encode(ssid),
                4096,
                256
            )
    return (binascii.hexlify(binkey)[0:64].decode('utf8'))