<template>
    <div class="navbar navbar-inverse navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container">
                <a class="brand">Segheria</a>

                <div class="nav-collapse collapse">
                    <ul class="nav">
                        <li :class="[tabPage == 'dashboard' ? 'active' : '']"><a @click="changeTab('dashboard');">Dashboard</a></li>
                        <li :class="[tabPage == 'lame' ? 'active' : '']"><a @click="changeTab('lame');">Gestione lame</a></li>
                        <li :class="[tabPage == 'excel' ? 'active' : '']"><a @click="changeTab('excel');">Esporta Excel</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>

    <div class="life">
        <div v-if="tagliato" class="ping orange"><span></span> Taglio</div>
        <div :class="['ping', online ? 'green' : 'red']"><span></span> {{ online ? "Online" : "Offline" }}</div>
    </div>
</template>

<script>
export default {
    name: 'Navbar',
    props: {
        online: {
            type: Boolean,
            default: true
        },
        tabPage: {
            type: String,
            default: 'dashboard'
        }
    },
    data() {
        return {
            tagliato: false
        }
    },
    computed: {
    },
    methods: {
        changeTab(name) {
            this.$emit('change-tab', name);
        },
        taglio() {
            this.tagliato = true;
            setTimeout(() => {
                this.tagliato = false;
            }, 1000);
        }
    },
    mounted() {
    }
}
</script>

<style scoped>
.navbar-inner {
    padding: 0 20px;
}

.navbar a {
    cursor: pointer;
}

.navbar-inner > .container {
    margin: 0;
}

.life {
    position: absolute;
    top: 0;
    right: 0;
    display: flex;
    align-items: center;
    z-index: 9999;
    height: 40px;
}

.ping {
    display: flex;
    align-items: center;
    color: #ffffffb9;
    font-size: 12px;
    margin-right: 14px;
}

.ping > span {
    width: 10px;
    height: 10px;
    margin-right: 8px;
    border-radius: 10px;
}

.ping.green > span {
    background-color: #62e662;
}

.ping.orange > span {
    background-color: #ffa167;
}

.ping.red > span {
    background-color: #f64b4b;
}
</style>