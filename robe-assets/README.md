## robe-assets

* Advanced and easy to use asset projects.

#### Motivation

Creating an asset bundle which can provide following points. 
* easy to understand 
* configurable 
* enough to serve common needs (classpath,filesystem,http)
* cache support

## Getting Started
You have to complete 2 steps in order to start serving you assets via robe-assets.

* Add dependency (Maven sample)

```xml
        <dependency>
            <groupId>com.mebitech.robe.assets</groupId>
            <artifactId>robe-assets</artifactId>
        </dependency>
```

* Define configuration <a name="FileAsset"></a>File Asset

    - File Asset Yml Configuration

    ```yml
    robe:
      assets:
        - resourcePath: ../../samples/robe-quickstart-frontend/build/
          uriPath: /
          indexFile: index.html
          assetsName: io.robe.admin.ui
          cached: false
          type: "filesystem"
    ```

    - Http Asset Yml Configuration

    ```yml
    robe:
      assets:
        - resourcePath: http://localhost:8081
          uriPath: /
          indexFile: index.html
          assetsName: io.robe.admin.ui
          cached: false
          type: "http"
    ```