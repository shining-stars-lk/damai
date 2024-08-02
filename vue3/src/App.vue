<template>
  <router-view :key="route.fullPath"/>
</template>
<script setup  >
import {useRoute, useRouter} from 'vue-router'
const route = useRoute();
</script>
