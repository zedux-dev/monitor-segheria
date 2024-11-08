<template>
    <navbar ref="navbar" :online="online" :tabPage="tab" @change-tab="changeTab($event)" />

    <div class="content">
        <Dashboard v-if="tab == 'dashboard'" ref="dashboard" :socket="socket" :database="database" @taglio="updateLama" />
        <GestioneLame v-if="tab == 'lame'" ref="gestoreLame" :socket="socket" :database="database" />
        <ExportExcel v-if="tab == 'excel'" />
    </div>

    <SelettoreLame v-if="tab == 'dashboard'" ref="selettoreLame" :socket="socket" :database="database" />
</template>

<script>
import Navbar from './components/Navbar.vue';
import SelettoreLame from './components/SelettoreLame.vue';
import Dashboard from './components/Dashboard.vue';
import GestioneLame from './components/GestioneLame.vue';
import ExportExcel from './components/ExportExcel.vue';
import { io } from 'socket.io-client';

export default {
    name: 'App',
    components: {
        Navbar,
        Dashboard,
        GestioneLame,
        ExportExcel,
        SelettoreLame
    },
    data() {
        return {
            tab: 'dashboard',
            socket: null,
            database: null,
            listeners: false,
            timeout: null,
            online: true,
            initier: null
        }
    },
    methods: {
        heartbeat() {
            if(this.socket.connected) {
                this.socket.emit('ping');
            }
        },
        setupListeners() {
            if(this.socket.connected) {
                this.socket.on('pong', () => {
                    if(this.timeout) {
                        clearTimeout(this.timeout);
                        this.timeout = null;
                        this.online = true;
                    }
                });
    
                this.socket.on('get-db', (db) => {
                    this.database = db;
                    setTimeout(() => {
                        this.$refs.selettoreLame.loadLame();
                    }, 100);
                });

                this.socket.on('taglio', (secondi) => {                
                    this.$refs.dashboard.newTaglio(parseInt(secondi));
                })

                this.listeners = true;
            }
        },
        getDatabase() {
            if(this.socket.connected) {
                this.socket.emit('get-db');
            }
        },
        changeTab(name) {
            this.tab = name;
        },
        updateLama() {
            this.$refs.navbar.taglio();
            this.$refs.gestoreLame.loadLame();
            this.$refs.selettoreLame.loadLame();
        }
    },
    mounted() {
        this.socket = io('http://localhost:8000');

        if(this.socket) {
            if(!this.initier) {
                this.initier = setInterval(() => {
                    if(!this.listeners) this.setupListeners();
                    if(!this.database) this.getDatabase();

                    if(this.listeners && this.database) {
                        clearInterval(this.initier);
                        this.initier = null;
                    }
                }, 100);
            }
            
            this.heartbeat();

            setInterval(() => {
                this.heartbeat();

                this.timeout = setTimeout(() => {
                    this.online = false;
                }, 2000);
            }, 5000);
        }
    }
}
</script>

<style scoped>
.content {
    width: 100%;
    display: flex;
    flex-direction: column;
    flex: 1;
    padding-top: 40px;
}
</style>