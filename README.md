# resource-bundle-i18n
Auto generate resource bundle translations

## Getting started
### Simple configuration
```groovy
plugins {
    //...
    id 'com.legyver.resource-bundle-i18n' version'1.0.0'
}

resourceBundleI18n {
    translationUrl = 'http://localhost:5000'
    targetLanguages =  ['en', 'en_US', 'en_GB', "es", "de", "fr"]
    bundleName = 'fully.qualified.bundle.name.with.package'
    //example bundle names:
    //if your your have a bundle called defaults in src\main\resources\my\bundle
    //then your bundle name would be my.bundle.defaults
}
```

### Complete configuration

```groovy
 resourceBundleI18n {
    translationUrl = "http://localhost:5000"
    targetLanguages = ["en", "es", "de", "fr"]
    bundleName = "com.example.bundle" //this will match src/main/resources/com/example/bundle.properties as the source
    client = "LIBRETRANSLATE" //the client to use to communicate with the translationUrl
            // ("LIBRETRANSLATE" is also the default value as it is the only client supported at this time)
    apiKey = "some key"
}
```