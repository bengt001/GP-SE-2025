<script setup lang="ts">

</script>

<template>
  <v-card
    class="mx-auto"
    width="500"
    border
    rounded="pill"
    flat
  >
    <template #text>
      <div class="px-4">
        <v-text-field
          density="compact"
          placeholder="Suchen"
          prepend-inner-icon="mdi-magnify"
          variant="solo"
          clearable
          flat
          hide-details
          single-line
        />
      </div>
    </template>
  </v-card>
</template>
