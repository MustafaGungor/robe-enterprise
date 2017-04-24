package com.mebitech.robe.asset;


import com.fasterxml.jackson.core.type.TypeReference;
import com.mebitech.robe.asset.file.FileAssetServlet;
import com.mebitech.robe.asset.http.HttpAssetServlet;
import com.mebitech.robe.asset.util.EncodingUtil;
import com.mebitech.robe.common.util.Strings;
import com.mebitech.robe.core.bundle.Bundle;
import com.mebitech.robe.core.bundle.BundleBean;
import com.mebitech.robe.core.bundle.BundleContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.List;

/**
 * Created by kamilbukum on 09/03/2017.
 */

public class AssetBundle extends Bundle<List<AssetProperties>, ServletContext> {
    private static final TypeReference<List<AssetProperties>> LIST_TYPE_REFERENCE = new TypeReference<List<AssetProperties>>() {};
    @Override
    public String getPropertyName() {
        return "robe.assets";
    }


    @Override
    public TypeReference<List<AssetProperties>> getTypeReference() {
        return LIST_TYPE_REFERENCE;
    }

    @Override
    public void onStart(List<AssetProperties> properties, BundleContext<ServletContext> bundleContext) {
        for (AssetProperties asset: properties) {
            AssetServlet servlet = null;
            switch (asset.getType()) {
                case filesystem:
                    servlet = new FileAssetServlet(asset, EncodingUtil.DEFAULT_MEDIA_TYPE.charset().get());
                    break;
                case http:
                    servlet =  new HttpAssetServlet(asset, EncodingUtil.DEFAULT_MEDIA_TYPE.charset().get());
                    break;
            }
            if(servlet != null) {
                BundleBean bundleBean = new BundleBean(Strings.unCapitalizeFirstChar(servlet.getClass().getSimpleName()), servlet);
                bundleContext.getListener().onCreate(bundleBean);
                ServletRegistration.Dynamic serviceServlet = bundleContext.getContext().addServlet(asset.getAssetsName(), servlet);
                serviceServlet.addMapping(servlet.getUriPath());
                serviceServlet.setAsyncSupported(true);
                serviceServlet.setLoadOnStartup(2);
            }
        }
    }
}
