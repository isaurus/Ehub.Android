package ehub.backend.domain.enums;

public enum Country {
    // Europa
    SPAIN(0),          // ES
    ENGLAND(1),        // GB (aunque es UK en general)
    GERMANY(2),        // DE
    FRANCE(3),         // FR
    ITALY(4),          // IT
    NETHERLANDS(5),    // NL
    PORTUGAL(6),       // PT
    POLAND(7),         // PL
    SWEDEN(8),         // SE
    NORWAY(9),         // NO
    DENMARK(10),       // DK
    BELGIUM(11),       // BE
    SWITZERLAND(12),   // CH
    IRELAND(13),       // IE
    AUSTRIA(14),       // AT
    FINLAND(15),       // FI
    GREECE(16),        // GR
    CZECHIA(17),       // CZ
    HUNGARY(18),       // HU
    ROMANIA(19),       // RO

    // América
    UNITED_STATES(20), // US
    CANADA(21),        // CA
    MEXICO(22),        // MX
    BRAZIL(23),        // BR
    ARGENTINA(24),     // AR
    COLOMBIA(25),      // CO
    CHILE(26),         // CL
    PERU(27),          // PE

    // Asia
    CHINA(28),         // CN
    JAPAN(29),         // JP
    INDIA(30),         // IN
    SOUTH_KOREA(31),   // KR
    SINGAPORE(32),     // SG
    UNITED_ARAB_EMIRATES(33), // AE
    SAUDI_ARABIA(34),  // SA
    TURKEY(35),        // TR

    // África
    SOUTH_AFRICA(36),  // ZA
    EGYPT(37),         // EG
    NIGERIA(38),       // NG
    MOROCCO(39),       // MA

    // Oceanía
    AUSTRALIA(40),     // AU
    NEW_ZEALAND(41);   // NZ

    private final int value;

    Country(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
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
}