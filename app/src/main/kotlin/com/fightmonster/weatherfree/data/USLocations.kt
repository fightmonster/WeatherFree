package com.fightmonster.weatherfree.data

// US States Data
data class USState(
    val name: String,
    val code: String
)

val USStates = listOf(
    USState("Alabama", "AL"),
    USState("Alaska", "AK"),
    USState("Arizona", "AZ"),
    USState("Arkansas", "AR"),
    USState("California", "CA"),
    USState("Colorado", "CO"),
    USState("Connecticut", "CT"),
    USState("Delaware", "DE"),
    USState("Florida", "FL"),
    USState("Georgia", "GA"),
    USState("Hawaii", "HI"),
    USState("Idaho", "ID"),
    USState("Illinois", "IL"),
    USState("Indiana", "IN"),
    USState("Iowa", "IA"),
    USState("Kansas", "KS"),
    USState("Kentucky", "KY"),
    USState("Louisiana", "LA"),
    USState("Maine", "ME"),
    USState("Maryland", "MD"),
    USState("Massachusetts", "MA"),
    USState("Michigan", "MI"),
    USState("Minnesota", "MN"),
    USState("Mississippi", "MS"),
    USState("Missouri", "MO"),
    USState("Montana", "MT"),
    USState("Nebraska", "NE"),
    USState("Nevada", "NV"),
    USState("New Hampshire", "NH"),
    USState("New Jersey", "NJ"),
    USState("New Mexico", "NM"),
    USState("New York", "NY"),
    USState("North Carolina", "NC"),
    USState("North Dakota", "ND"),
    USState("Ohio", "OH"),
    USState("Oklahoma", "OK"),
    USState("Oregon", "OR"),
    USState("Pennsylvania", "PA"),
    USState("Rhode Island", "RI"),
    USState("South Carolina", "SC"),
    USState("South Dakota", "SD"),
    USState("Tennessee", "TN"),
    USState("Texas", "TX"),
    USState("Utah", "UT"),
    USState("Vermont", "VT"),
    USState("Virginia", "VA"),
    USState("Washington", "WA"),
    USState("West Virginia", "WV"),
    USState("Wisconsin", "WI"),
    USState("Wyoming", "WY"),
    USState("District of Columbia", "DC"),
    USState("Puerto Rico", "PR"),
    USState("Guam", "GU"),
    USState("American Samoa", "AS"),
    USState("Northern Mariana Islands", "MP"),
    USState("Virgin Islands", "VI")
)

// US Cities Data with coordinates
data class USCity(
    val name: String,
    val state: String,
    val zip: String,
    val coordinates: Pair<Double, Double>
)

// Major US Cities by State with coordinates
// These coordinates are approximate city centers for NWS API
val USCities = mapOf(
    "AL" to listOf(
        USCity("Birmingham", "AL", "35203", 33.5207 to -86.8025),
        USCity("Montgomery", "AL", "36101", 32.3617 to -86.2792),
        USCity("Huntsville", "AL", "35801", 34.7304 to -86.5861),
        USCity("Mobile", "AL", "36601", 30.6944 to -88.0431)
    ),
    "AK" to listOf(
        USCity("Anchorage", "AK", "99501", 61.2181 to -149.9003),
        USCity("Fairbanks", "AK", "99701", 64.8378 to -147.7164),
        USCity("Juneau", "AK", "99801", 58.3019 to -134.4197)
    ),
    "AZ" to listOf(
        USCity("Phoenix", "AZ", "85001", 33.4484 to -112.0740),
        USCity("Tucson", "AZ", "85701", 32.2226 to -110.9747),
        USCity("Mesa", "AZ", "85201", 33.4152 to -111.8315),
        USCity("Scottsdale", "AZ", "85251", 33.4942 to -111.9261)
    ),
    "AR" to listOf(
        USCity("Little Rock", "AR", "72201", 34.7465 to -92.2896),
        USCity("Fort Smith", "AR", "72901", 35.3859 to -94.3684),
        USCity("Fayetteville", "AR", "72701", 36.0726 to -94.1573)
    ),
    "CA" to listOf(
        USCity("Los Angeles", "CA", "90210", 34.0522 to -118.2437),
        USCity("San Francisco", "CA", "94102", 37.7749 to -122.4194),
        USCity("San Diego", "CA", "92101", 32.7157 to -117.1611),
        USCity("San Jose", "CA", "95101", 37.3382 to -121.8863),
        USCity("Sacramento", "CA", "94203", 38.5816 to -121.4944),
        USCity("Oakland", "CA", "94601", 37.8044 to -122.2712),
        USCity("Fresno", "CA", "93701", 36.7468 to -119.7726)
    ),
    "CO" to listOf(
        USCity("Denver", "CO", "80201", 39.7392 to -104.9903),
        USCity("Colorado Springs", "CO", "80901", 38.8339 to -104.8214),
        USCity("Aurora", "CO", "80011", 39.7295 to -104.8319),
        USCity("Fort Collins", "CO", "80521", 40.5853 to -105.0844)
    ),
    "CT" to listOf(
        USCity("Hartford", "CT", "06101", 41.7658 to -72.6734),
        USCity("New Haven", "CT", "06510", 41.3083 to -72.9279),
        USCity("Stamford", "CT", "06901", 41.0534 to -73.5387)
    ),
    "DE" to listOf(
        USCity("Wilmington", "DE", "19801", 39.7459 to -75.5466),
        USCity("Dover", "DE", "19901", 39.1619 to -75.5264)
    ),
    "FL" to listOf(
        USCity("Miami", "FL", "33101", 25.7617 to -80.1918),
        USCity("Jacksonville", "FL", "32201", 30.3322 to -81.6557),
        USCity("Tampa", "FL", "33601", 27.9506 to -82.4572),
        USCity("Orlando", "FL", "32801", 28.5383 to -81.3792),
        USCity("Tallahassee", "FL", "32301", 30.4383 to -84.2807)
    ),
    "GA" to listOf(
        USCity("Atlanta", "GA", "30301", 33.7490 to -84.3880),
        USCity("Savannah", "GA", "31401", 32.0835 to -81.0998),
        USCity("Augusta", "GA", "30901", 33.4700 to -82.0151)
    ),
    "HI" to listOf(
        USCity("Honolulu", "HI", "96801", 21.3070 to -157.8581),
        USCity("Pearl City", "HI", "96782", 21.3984 to -157.9977)
    ),
    "ID" to listOf(
        USCity("Boise", "ID", "83701", 43.6150 to -116.2023),
        USCity("Coeur d'Alene", "ID", "83814", 46.8777 to -116.7805)
    ),
    "IL" to listOf(
        USCity("Chicago", "IL", "60601", 41.8781 to -87.6298),
        USCity("Springfield", "IL", "62701", 39.8017 to -89.6437),
        USCity("Peoria", "IL", "61601", 40.6936 to -89.5890),
        USCity("Rockford", "IL", "61101", 42.2711 to -89.0940)
    ),
    "IN" to listOf(
        USCity("Indianapolis", "IN", "46201", 39.7684 to -86.1581),
        USCity("Fort Wayne", "IN", "46801", 41.0793 to -85.1394),
        USCity("South Bend", "IN", "46601", 41.6774 to -86.2506)
    ),
    "IA" to listOf(
        USCity("Des Moines", "IA", "50301", 41.6005 to -93.6091),
        USCity("Cedar Rapids", "IA", "52401", 41.8768 to -91.6656),
        USCity("Davenport", "IA", "52801", 41.5236 to -90.7676)
    ),
    "KS" to listOf(
        USCity("Wichita", "KS", "67201", 37.6872 to -97.3301),
        USCity("Topeka", "KS", "66601", 39.0558 to -95.6890),
        USCity("Kansas City", "KS", "66101", 39.0997 to -94.5786)
    ),
    "KY" to listOf(
        USCity("Louisville", "KY", "40201", 38.2542 to -85.7594),
        USCity("Lexington", "KY", "40501", 38.0458 to -84.5037),
        USCity("Bowling Green", "KY", "42101", 36.9967 to -86.4434)
    ),
    "LA" to listOf(
        USCity("New Orleans", "LA", "70101", 29.9511 to -90.0715),
        USCity("Baton Rouge", "LA", "70801", 30.4515 to -91.1871),
        USCity("Shreveport", "LA", "71101", 32.4751 to -93.7502)
    ),
    "MA" to listOf(
        USCity("Boston", "MA", "02101", 42.3601 to -71.0589),
        USCity("Worcester", "MA", "01601", 42.2626 to -71.8023),
        USCity("Springfield", "MA", "01101", 42.1015 to -72.5898),
        USCity("Cambridge", "MA", "02139", 42.3601 to -71.0589)
    ),
    "ME" to listOf(
        USCity("Portland", "ME", "04101", 43.6591 to -70.2568),
        USCity("Augusta", "ME", "04330", 43.9967 to -69.7785),
        USCity("Bangor", "ME", "04401", 44.8011 to -68.7676)
    ),
    "MD" to listOf(
        USCity("Baltimore", "MD", "21201", 39.2904 to -76.6122),
        USCity("Annapolis", "MD", "21401", 38.9784 to -76.4918),
        USCity("Silver Spring", "MD", "20901", 39.0053 to -77.0231)
    ),
    "MI" to listOf(
        USCity("Detroit", "MI", "48201", 42.3314 to -83.0458),
        USCity("Grand Rapids", "MI", "49501", 42.9664 to -85.6681),
        USCity("Lansing", "MI", "48901", 42.7325 to -84.5553),
        USCity("Ann Arbor", "MI", "48103", 42.2808 to -83.7430)
    ),
    "MN" to listOf(
        USCity("Minneapolis", "MN", "55401", 44.9778 to -93.2650),
        USCity("Saint Paul", "MN", "55101", 44.9537 to -93.0900),
        USCity("Rochester", "MN", "55901", 44.0215 to -92.4699)
    ),
    "MS" to listOf(
        USCity("Jackson", "MS", "39201", 32.2988 to -90.1848),
        USCity("Gulfport", "MS", "39501", 30.3674 to -89.0928),
        USCity("Biloxi", "MS", "39530", 30.3960 to -88.8863)
    ),
    "MO" to listOf(
        USCity("Kansas City", "MO", "64101", 39.0997 to -94.5786),
        USCity("St. Louis", "MO", "63101", 38.6270 to -90.1994),
        USCity("Springfield", "MO", "65801", 37.2153 to -93.2980)
    ),
    "MT" to listOf(
        USCity("Billings", "MT", "59101", 45.7833 to -108.5007),
        USCity("Missoula", "MT", "59801", 46.8772 to -114.0133),
        USCity("Great Falls", "MT", "59401", 47.5029 to -111.3008)
    ),
    "NE" to listOf(
        USCity("Omaha", "NE", "68101", 41.2565 to -95.9345),
        USCity("Lincoln", "NE", "68501", 40.8136 to -96.7026),
        USCity("Bellevue", "NE", "68005", 41.1346 to -95.9333")
    ),
    "NV" to listOf(
        USCity("Las Vegas", "NV", "89101", 36.1699 to -115.1398),
        USCity("Reno", "NV", "89501", 39.5296 to -119.8138),
        USCity("Henderson", "NV", "89002", 36.0629 to -114.9817)
    ),
    "NH" to listOf(
        USCity("Manchester", "NH", "03101", 42.9956 to -71.4548),
        USCity("Nashua", "NH", "03060", 42.7646 to -71.4626),
        USCity("Concord", "NH", "03301", 43.2081 to -71.5378")
    ),
    "NJ" to listOf(
        USCity("Newark", "NJ", "07101", 40.7357 to -74.1724),
        USCity("Jersey City", "NJ", "07302", 40.7178 to -74.0432),
        USCity("Paterson", "NJ", "07501", 40.9168 to -74.1718"),
        USCity("Trenton", "NJ", "08601", 40.2177 to -74.7429")
    ),
    "NM" to listOf(
        USCity("Albuquerque", "NM", "87101", 35.0844 to -106.6504),
        USCity("Santa Fe", "NM", "87501", 35.6870 to -105.9379),
        USCity("Las Cruces", "NM", "88001", 32.3122 to -106.7758")
    ),
    "NY" to listOf(
        USCity("New York", "NY", "10001", 40.7128 to -74.0060),
        USCity("Buffalo", "NY", "14201", 42.8864 to -78.8784),
        USCity("Rochester", "NY", "14601", 43.1566 to -77.6088),
        USCity("Albany", "NY", "12201", 42.6526 to -73.7562),
        USCity("Syracuse", "NY", "13201", 43.0481 to -76.1474")
    ),
    "OH" to listOf(
        USCity("Columbus", "OH", "43201", 39.9612 to -82.9988),
        USCity("Cleveland", "OH", "44101", 41.4993 to -81.6944),
        USCity("Cincinnati", "OH", "45201", 39.1031 to -84.5120),
        USCity("Toledo", "OH", "43601", 41.6528 to -83.5379")
    ),
    "OK" to listOf(
        USCity("Oklahoma City", "OK", "73101", 35.4676 to -97.5164),
        USCity("Tulsa", "OK", "74101", 36.1540 to -95.9928),
        USCity("Norman", "OK", "73019", 41.2981 to -97.4395")
    ),
    "OR" to listOf(
        USCity("Portland", "OR", "97201", 45.5152 to -122.6784),
        USCity("Eugene", "OR", "97401", 44.0215 to -123.0868),
        USCity("Salem", "OR", "97301", 44.9429 to -123.0322),
        USCity("Gresham", "OR", "97080", 45.5007 to -122.4526")
    ),
    "PA" to listOf(
        USCity("Philadelphia", "PA", "19101", 39.9526 to -75.1652),
        USCity("Pittsburgh", "PA", "15201", 40.4406 to -79.9959),
        USCity("Allentown", "PA", "18101", 40.6026 to -75.4791),
        USCity("Erie", "PA", "16501", 42.1292 to -80.0851")
    ),
    "RI" to listOf(
        USCity("Providence", "RI", "02901", 41.8240 to -71.4128),
        USCity("Warwick", "RI", "02886", 41.7663 to -71.4332"),
        USCity("Cranston", "RI", "02920", 41.7663 to -71.4332")
    ),
    "SC" to listOf(
        USCity("Columbia", "SC", "29201", 34.0007 to -81.0348),
        USCity("Charleston", "SC", "29401", 32.7766 to -79.9312),
        USCity("Greenville", "SC", "29601", 34.8526 to -82.3940")
    ),
    "SD" to listOf(
        USCity("Sioux Falls", "SD", "57101", 43.5698 to -96.7311),
        USCity("Rapid City", "SD", "57701", 44.6808 to -103.0133),
        USCity("Aberdeen", "SD", "57401", 45.4646 to -96.9858")
    ),
    "TN" to listOf(
        USCity("Nashville", "TN", "37201", 36.1627 to -86.7816),
        USCity("Memphis", "TN", "38101", 35.1495 to -90.0490),
        USCity("Knoxville", "TN", "37901", 35.9606 to -83.9208"),
        USCity("Chattanooga", "TN", "37401", 35.0456 to -85.3097")
    ),
    "TX" to listOf(
        USCity("Houston", "TX", "77001", 29.7604 to -95.3698),
        USCity("Dallas", "TX", "75201", 32.7767 to -96.7970),
        USCity("San Antonio", "TX", "78201", 29.4241 to -98.4936),
        USCity("Austin", "TX", "78701", 30.2672 to -97.7431),
        USCity("Fort Worth", "TX", "76101", 32.7574 to -97.3208),
        USCity("El Paso", "TX", "79901", 31.7619 to -106.4850")
    ),
    "UT" to listOf(
        USCity("Salt Lake City", "UT", "84101", 40.7608 to -111.8910),
        USCity("Provo", "UT", "84601", 40.2338 to -111.6585),
        USCity("Ogden", "UT", "84401", 41.2225 to -111.9730")
    ),
    "VT" to listOf(
        USCity("Burlington", "VT", "05401", 44.4759 to -73.2121),
        USCity("South Burlington", "VT", "05403", 44.4668 to -73.1872),
        USCity("Rutland", "VT", "05701", 43.6106 to -72.6726")
    ),
    "VA" to listOf(
        USCity("Virginia Beach", "VA", "23451", 36.8529 to -76.9774),
        USCity("Norfolk", "VA", "23501", 36.8468 to -76.2852),
        USCity("Richmond", "VA", "23219", 37.5407 to -77.4360),
        USCity("Arlington", "VA", "22201", 38.8815 to -77.1043")
    ),
    "WA" to listOf(
        USCity("Seattle", "WA", "98101", 47.6062 to -122.3321),
        USCity("Spokane", "WA", "99201", 47.6588 to -117.4260),
        USCity("Tacoma", "WA", "98401", 47.2529 to -122.4443),
        USCity("Bellevue", "WA", "98004", 47.6101 to -122.2015")
    ),
    "WV" to listOf(
        USCity("Charleston", "WV", "25301", 38.3498 to -81.6326),
        USCity("Huntington", "WV", "25701", 38.4192 to -82.4452),
        USCity("Parkersburg", "WV", "26101", 39.0114 to -81.5873")
    ),
    "WI" to listOf(
        USCity("Milwaukee", "WI", "53201", 43.0389 to -87.9065),
        USCity("Madison", "WI", "53701", 43.0731 to -89.4012"),
        USCity("Green Bay", "WI", "54301", 44.5133 to -88.0133")
    ),
    "WY" to listOf(
        USCity("Cheyenne", "WY", "82001", 41.1400 to -104.8197),
        USCity("Casper", "WY", "82601", 42.8666 to -106.3131),
        USCity("Laramie", "WY", "82070", 41.3114 to -105.5909")
    ),
    "DC" to listOf(
        USCity("Washington", "DC", "20001", 38.9072 to -77.0369")
    ),
    "PR" to listOf(
        USCity("San Juan", "PR", "00901", 18.4663 to -66.1057)
    )
)
