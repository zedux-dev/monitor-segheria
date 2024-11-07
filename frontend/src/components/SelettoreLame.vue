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
</template>

<script>
export default {
    name: 'SelettoreLame',
    props: {
        tabPage: {
            type: String,
            default: 'dashboard'
        }
    },
    data() {
        return {
        }
    },
    computed: {
    },
    methods: {
        changeTab(name) {
            this.$emit('change-tab', name);
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
</style>