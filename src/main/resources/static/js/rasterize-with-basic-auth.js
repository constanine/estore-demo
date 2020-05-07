var page = require('webpage').create(),
    system = require('system'),
    userid, upassword, address, output, size;
	
	userid=system.args[1];
	upassword=system.args[2];
	page.settings.userName = userid;
	page.settings.password = upassword;

if(userid == 'xialj'){
	 console.log('username is the same');
}
	
if (system.args.length < 4 || system.args.length > 6) {
    console.log('Usage: rasterize.js URL filename [paperwidth*paperheight|paperformat] [zoom]');
    console.log('  paper (pdf output) examples: "5in*7.5in", "10cm*20cm", "A4", "Letter"');
    phantom.exit(1);
} else {
    address = system.args[3];
    output = system.args[4];
    page.viewportSize = { width: 600, height: 600 };
    if (system.args.length > 3 && system.args[4].substr(-4) === ".pdf") {
        size = system.args[5].split('*');
        page.paperSize = size.length === 2 ? { width: size[0], height: size[1], margin: '0px' }
                                           : { format: system.args[5], orientation: 'portrait', margin: '1cm' };
    }
    if (system.args.length > 6) {
        page.zoomFactor = system.args[6];
    }
    page.open(address, function (status) {
        if (status !== 'success') {
            console.log('Unable to load the address!');
            phantom.exit();
        } else {
            window.setTimeout(function () {
                page.render(output);
                phantom.exit();
            }, 200);
        }
    });
}
