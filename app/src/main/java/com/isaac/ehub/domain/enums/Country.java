package ehub.backend.domain.enums;

public enum Country {
    // Europa
    SPAIN(0, "ES"),          // ES
    ENGLAND(1, "GB"),        // GB (aunque es UK en general)
    GERMANY(2, "DE"),        // DE
    FRANCE(3, "FR"),         // FR
    ITALY(4, "IT"),          // IT
    NETHERLANDS(5, "NL"),    // NL
    PORTUGAL(6, "PT"),       // PT
    POLAND(7, "PL"),         // PL
    SWEDEN(8, "SE"),         // SE
    NORWAY(9, "NO"),         // NO
    DENMARK(10, "DK"),       // DK
    BELGIUM(11, "BE"),       // BE
    SWITZERLAND(12, "CH"),   // CH
    IRELAND(13, "IE"),       // IE
    AUSTRIA(14, "AT"),       // AT
    FINLAND(15, "FI"),       // FI
    GREECE(16, "GR"),        // GR
    CZECHIA(17, "CZ"),       // CZ
    HUNGARY(18, "HU"),       // HU
    ROMANIA(19, "RO"),       // RO

    // América
    UNITED_STATES(20, "US"), // US
    CANADA(21, "CA"),        // CA
    MEXICO(22, "MX"),        // MX
    BRAZIL(23, "BR"),        // BR
    ARGENTINA(24, "AR"),     // AR
    COLOMBIA(25, "CO"),      // CO
    CHILE(26, "CL"),         // CL
    PERU(27, "PE"),          // PE

    // Asia
    CHINA(28, "CN"),         // CN
    JAPAN(29, "JP"),         // JP
    INDIA(30, "IN"),         // IN
    SOUTH_KOREA(31, "KR"),   // KR
    SINGAPORE(32, "SG"),     // SG
    UNITED_ARAB_EMIRATES(33, "AE"), // AE
    SAUDI_ARABIA(34, "SA"),  // SA
    TURKEY(35, "TR"),        // TR

    // África
    SOUTH_AFRICA(36, "ZA"),  // ZA
    EGYPT(37, "EG"),         // EG
    NIGERIA(38, "NG"),       // NG
    MOROCCO(39, "MA"),       // MA

    // Oceanía
    AUSTRALIA(40, "AU"),     // AU
    NEW_ZEALAND(41, "NZ");   // NZ

    private final int value;
    private final String code;

    Country(int value, String code) {
        this.value = value;
        this.code = code;
    }

    public int getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    // Opcional: método para obtener el enum a partir del valor numérico
    public static Country fromValue(int value) {
        for (Country country : values()) {
            if (country.value == value) {
                return country;
            }
        }
        throw new IllegalArgumentException("No country with value " + value);
    }

    // Opcional: método para obtener el enum a partir del código
    public static Country fromCode(String code) {
        for (Country country : values()) {
            if (country.code.equalsIgnoreCase(code)) {
                return country;
            }
        }
        throw new IllegalArgumentException("No country with code " + code);
    }
}