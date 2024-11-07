import { createApp } from 'vue';
import './style.css';
import App from './App.vue';
import authInfo from '../../.tmp/auth_info.json';
import Neutralino from '@neutralinojs/lib';

if(import.meta.env.DEV) {
    window.NL_PORT = authInfo.nlPort;
    window.NL_TOKEN = authInfo.nlToken;
    window.NL_ARGS = [
        'bin\\neutralino-win_x64.exe',
        '',
        '--load-dir-res',
        '--path=.',
        '--export-auth-info',
        '--neu-dev-extension',
        '--neu-dev-auto-reload',
        '--window-enable-inspector'
    ];
}

createApp(App).mount('#app')

Neutralino.init();