const webpack = require("webpack");
module.exports = {
    devServer: {
        port: 7000
    },
    publicPath: '/',
    configureWebpack: {
        plugins: [
            new webpack.ProvidePlugin({
                $: "jquery",
                jQuery: "jquery",
                "window.jQuery": "jquery",
                "window.$": "jquery",
                Popper: ["popper.js", "default"]
            })
        ]
    }
}