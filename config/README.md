Required here is a file called `config.json` containing the Meteomatics API configuration and a file called `here.json` containing the Here Maps API configuration.

Meteomatics:
```json
{ 
    "api_url": "api.meteomatics.com/", 
    "api_user": "[username]", 
    "api_password": "[password]", 
    "api_parameters": [ 
        "t_2m:C", 
        "wind_gusts_10m_1h:kmh", 
        "wind_speed_10m:kmh", 
        "wind_dir_10m:d", 
        "precip_1h:mm", 
        "prob_precip_1h:p", 
        "significant_wave_height:m", 
        "peak_wave_period:s", 
        "tidal_amplitude:cm", 
        "visibility:km", 
        "weather_code_1h:idx" 
    ], 
    "api_period": 15, 
    "api_interval": 7, 
    "cache": "cache.json", 
    "disable_requests": false 
}
```

Here Maps:
```json
{
    "api_url": "https://places.cit.api.here.com/places/v1/autosuggest",
    "app_id": "[id]",
    "app_code": "[code]",
    "cache": "locations.json",
    "disable_requests": false
}
```
