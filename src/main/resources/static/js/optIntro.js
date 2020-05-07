/**
 * Created by Vincent on 2014/5/15.
 */
YIGO.ui.portal.intro = function(force){
    var version = 1;
    var cKey = 'yigoPortalIntro'+version;
    var start = document.cookie.indexOf(cKey),end;
    var date = new Date();
    date.setTime(date.getTime() + 30*24*60*60*1000);
    if(start<0)
        document.cookie = cKey + '=1;expires='+ date.toGMTString();
    else{
        end = document.cookie.indexOf(';',start);
        var value = document.cookie.substring(start,end);
        var visitTime = value.split('=')[1];
        if(visitTime>0 && !force){
            document.cookie = cKey + '=' + (Number(visitTime)+1)+";expires="+ date.toGMTString();
            return;
        }
        else
            document.cookie = cKey + '=' + (Number(visitTime)+1)+";expires="+ date.toGMTString();
    }

    $.getScript('../js/intro.js-0.8.0/intro.min.js',function(){
        var intro = introJs();
        intro.setOptions({
            nextLabel : '继续',
            prevLabel : '上一步',
            skipLabel : '退出引导',
            doneLabel : '结束',
            exitOnOverlayClick : false,
            steps: [
                {
                    intro: "<b>您好!Portal页面已更新，简单介绍一下！</b><p>回车和方向键控制步骤，ESC键退出。点击下面小圆点可直接跳至该步骤</p>"
                },
                {
                    element: document.querySelector('.head'),
                    intro: "<b>页面头部空间缩小至原来的一半</b>"
                },
                {
                    element: document.querySelector('.p_logo'),
                    intro: "<b>替换掉YIGO Cloud logo</b>"
                },
                {
                    element: document.querySelector('.funcEntrance'),
                    intro: "<b>功能导航相对位置保持一致</b>"
                },
                {
                    element: '搜索面板',
                    intro:'<dt>输入框</dt><dd>可以搜索当前用户可以使用的菜单入口。</dd><dd>支持汉字拼音及模糊查询。</dd><dd>例如想要找到"工作流执行图"这个入口，可以输入"工作执行","gzzhix","工作zhixing"进行搜索。</dd><dd>搜索结果显示六条记录，项目中可根据需求增加或减少。</dd>'
                        +'<dt>常用功能</dt><dd>根据用户打开菜单入口的频率，呈现出最频繁使用的入口。默认显示四个，可根据需求调整此数量。</dd><dd>此项功能需要HTML5 INDEXDB支持，如果您无法使用此功能，请根据面板里的提示升级浏览器。</dd>'
                },
                {
                    element: '菜单',
                    intro:"<b>Allmenu菜单</b>"
                },
                {
                    element: '#logInfo',
                    intro: '<dt>用户信息</dt><dd>这里只显示用户名，<em>请将鼠标悬停至用户名，</em>可以看见整个登录信息菜单。</dd><dd>这里呈现后台配置statusBar的所有内容</dd><dd><em>"退出系统"</em>的功能也被移动到这个悬浮面板当中。</dd>',
                    position: 'left'
                },
                {
                    element: '#portalOpts',
                    intro: '<dt>设置</dt><dd><em>请将鼠标悬停至此</em>，可以看见设置功能面板。</dd><dd>目前有"修改密码"和"RebuilAll"，添加了操作向导重播按钮，项目上也可根据自己的需求二次开发添加或删除相关功能。</dd>',
                    position: 'left'
                },
                {
                    element: '.p_treeMenu_trigger',
                    intro: '<dt>树形菜单按钮</dt><dd>默认情况下该页面中并不直接显示树形allMenu，通过点击此按钮可触发显示隐藏的树形菜单。</dd><dd>项目上可以通过相关配置决定使用哪种菜单UI，目前新WEB已经提供三种allMenu的渲染方式。</dd>',
                    position:'right'
                },
                {
                    element: '树形菜单',
                    intro: '<dt>树形菜单</dt><dd>与applet版本操作相似的树形allMenu。</dd><dd>搜索功能目前只能搜索汉字，<em>输入关键字以后按下回车即可。</em></dd><dd>加入了便捷的<em>"关注单据"</em>功能，菜单中"关注菜单"节点中罗列了用户关注过的具体单据，例如张三在某天填写的某一张出库通知单。</dd><dd>"关注单据"目前暂时为隐藏功能，打开具体单据后<em>按下"Ctrl+K"添加关注，"Ctrl+L"取消关注</em>。</dd>'
                },
                {
                    intro: '<b>开始！</b><p><a href="http://scmhacks.yi.org:8000/trac/yigo/wiki/WikiStart" target="_blank">点击查阅更多版本更新信息</a></p><p>Tips:该向导只会在出现重要更新时第一次访问出现，重播功能在页面右上角的设置中</p>'
                }
            ]
        });
        intro.onchange(function() {
            switch(this._currentStep){
                case 3:
                    var menuNav = YIGO.portalCompMgr.get('allMenuNav');
                    if(menuNav && menuNav.menu.el)
                        menuNav.hideMenu();
                    break;
                case 4:
                    var menuNav = YIGO.portalCompMgr.get('allMenuNav');
                    if(menuNav)
                        menuNav.showMenu();
                    this._introItems[4].element = $('.p_menu_srchPanel')[0];
                    this._introItems[4].position='right';
                    break;
                case 5:
                    var menuNav = YIGO.portalCompMgr.get('allMenuNav');
                    if(menuNav)
                        menuNav.showMenu();
                    this._introItems[5].element = $('.y_menu_navBox')[0];
                    this._introItems[5].position='right';
                    break;
                case 6:
                    var menuNav = YIGO.portalCompMgr.get('allMenuNav');
                    if(menuNav)
                        menuNav.hideMenu();
                    break;
                case 8:
                    var treeMenuPanel = YIGO.portalCompMgr.get('treeMenuPanel');
                    if(treeMenuPanel)
                        treeMenuPanel.expandPanel(false);
                    break;
                case 9:
                    var treeMenuPanel = YIGO.portalCompMgr.get('treeMenuPanel');
                    if(treeMenuPanel)
                    treeMenuPanel.el.style.left=0;
                    this._introItems[9].element = treeMenuPanel.el;
                    this._introItems[9].position='right';
                    break;
                case 10:
                    var treeMenuPanel = YIGO.portalCompMgr.get('treeMenuPanel');
                    treeMenuPanel.expandPanel(false);
                    break;
            }
        });
        intro.onexit(function(){
            if(YIGO.portalCompMgr.get('treeMenuPanel'))
                YIGO.portalCompMgr.get('treeMenuPanel').expandPanel(false);
            if(YIGO.portalCompMgr.get('allMenuNav'))
                YIGO.portalCompMgr.get('allMenuNav').hideMenu();
        })
        intro.start();
    })

}

