// share.js

// url - 需要分享的页面地址（当前页面地址）
// title - 分享的标题（文章标题）

export function toQQzone (url, title) {
    url = encodeURIComponent(url)
    title = encodeURIComponent(title)
    window.open(`https://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url=${url}&title=${title}&desc=${title}&summary=${title}&site=${url}`)
}

export function toQQ (url, title) {
    url = encodeURIComponent(url)
    title = encodeURIComponent(title)
    window.open(`https://connect.qq.com/widget/shareqq/index.html?url=${url}&title=${title}&source=${url}&desc=${title}&pics=`)
}

export function toWeibo (url, title) {
    url = encodeURIComponent(url)
    title = encodeURIComponent(title)
    window.open(`https://service.weibo.com/share/share.php?url=${url}&title=${title}&pic=&appkey=&sudaref=`)
}

export function toWechat (url, title) {
    url = encodeURIComponent(url)
    title = encodeURIComponent(title)
    // console.log(21, process.env.NUXT_ENV_APISERVER)
    window.open(`${process.env.NUXT_ENV_APISERVER}/sharetoWechat?url=${url}&title=${title}`)
}