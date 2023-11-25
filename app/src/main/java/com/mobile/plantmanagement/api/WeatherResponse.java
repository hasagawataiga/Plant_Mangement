package com.mobile.plantmanagement.api;

import com.google.gson.annotations.SerializedName;

import org.checkerframework.checker.index.qual.SearchIndexBottom;

import java.util.List;

public class WeatherResponse {
    @SerializedName("cod")
    private int cod;
    @SerializedName("message")
    private String message;
    @SerializedName("cnt")
    private int cnt;
    @SerializedName("list")
    private List<WeatherAllData> weatherAllDataList;
    @SerializedName("city")
    private WeatherCity weatherCity;
    public List<WeatherAllData> getWeatherAllDataList() {
        return weatherAllDataList;
    }

    public int getCod() {
        return cod;
    }

    public String getMessage() {
        return message;
    }

    public int getCnt() {
        return cnt;
    }

    public WeatherCity getWeatherCity() {
        return weatherCity;
    }

    public class WeatherAllData {
        @SerializedName("dt")
        private long dt;
        @SerializedName("main")
        private WeatherMainData weatherMainData;
        @SerializedName("weather")
        private List<WeatherDescription> weatherDescriptionList;
        @SerializedName("clouds")
        private Clouds clouds;
        @SerializedName("wind")
        private Wind wind;
        @SerializedName("visibility")
        private int visibility;
        @SerializedName("pop")
        private float pop;
        @SerializedName("rain")
        private Rain rain;
        @SerializedName("snow")
        private Snow snow;
        @SerializedName("sys")
        private WeatherSys weatherSys;
        @SerializedName("dt_txt")
        private String dt_txt;

        public long getDt() {
            return dt;
        }

        public WeatherMainData getWeatherMainData() {
            return weatherMainData;
        }

        public List<WeatherDescription> getWeatherDescription() {
            return weatherDescriptionList;
        }

        public Clouds getClouds() {
            return clouds;
        }

        public Wind getWind() {
            return wind;
        }

        public int getVisibility() {
            return visibility;
        }

        public float getPop() {
            return pop;
        }

        public WeatherSys getWeatherSys() {
            return weatherSys;
        }

        public String getDt_txt() {
            return dt_txt;
        }

        public class WeatherMainData {
            @SerializedName("temp")
            private float temp;
            @SerializedName("feels_like")
            private float feelsLike;
            @SerializedName("temp_min")
            private float tempMin;
            @SerializedName("temp_max")
            private float tempMax;
            @SerializedName("pressure")
            private int pressure;
            @SerializedName("sea_level")
            private int seaLevel;
            @SerializedName("grnd_level")
            private int grndLevel;
            @SerializedName("humidity")
            private int humidity;
            @SerializedName("temp_kf")
            private float tempKf;

            public float getTemp() {
                return temp;
            }

            public float getFeelsLike() {
                return feelsLike;
            }

            public float getTempMin() {
                return tempMin;
            }

            public float getTempMax() {
                return tempMax;
            }

            public int getPressure() {
                return pressure;
            }

            public int getSeaLevel() {
                return seaLevel;
            }

            public int getGrndLevel() {
                return grndLevel;
            }

            public int getHumidity() {
                return humidity;
            }

            public float getTempKf() {
                return tempKf;
            }
        }

        public class WeatherDescription {
            @SerializedName("id")
            private int id;
            @SerializedName("main")
            private String main;
            @SerializedName("description")
            private String description;
            @SerializedName("icon")
            private String icon;

            public int getId() {
                return id;
            }

            public String getMain() {
                return main;
            }

            public String getDescription() {
                return description;
            }

            public String getIcon() {
                return icon;
            }
        }

        public class Clouds {
            @SerializedName("all")
            private int all;

            public int getAll() {
                return all;
            }
        }

        public class Wind {
            @SerializedName("speed")
            private float speed;
            @SerializedName("deg")
            private int deg;
            @SerializedName("gust")
            private float gust;

            public float getSpeed() {
                return speed;
            }

            public int getDeg() {
                return deg;
            }

            public float getGust() {
                return gust;
            }
        }

        public class Rain {
            @SerializedName("3h")
            private float rainVolume;

            public float getRainVolume() {return this.rainVolume;}
        }

        public class Snow {
            @SerializedName("3h")
            private float snowVolume;

            public float getSnowVolume() {return this.snowVolume;}
        }

        public class WeatherSys {
            @SerializedName("pod")
            private char pod;

            public char getPod() {
                return pod;
            }
        }
    }

    public class WeatherCity {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("coord")
        private WeatherCityCoordination weatherCityCoordination;
        @SerializedName("country")
        private String country;
        @SerializedName("population")
        private long population;
        @SerializedName("timezone")
        private int timezone;
        @SerializedName("sunrise")
        private long sunrise;
        @SerializedName("sunset")
        private long sunset;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public WeatherCityCoordination getWeatherCityCoordination() {
            return weatherCityCoordination;
        }

        public String getCountry() {
            return country;
        }

        public long getPopulation() {
            return population;
        }

        public int getTimezone() {
            return timezone;
        }

        public long getSunrise() {
            return sunrise;
        }

        public long getSunset() {
            return sunset;
        }

        public class WeatherCityCoordination {
            @SerializedName("lat")
            private float lat;
            @SerializedName("lon")
            private float lon;

            public float getLat() {
                return lat;
            }

            public float getLon() {
                return lon;
            }
        }
    }
}














