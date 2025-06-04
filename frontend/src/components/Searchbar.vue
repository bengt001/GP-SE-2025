<script lang="ts">
export default {
  emits: ['changeValue'],
  data () {
    return {
      searchValue: ""
    }
  },
  methods: {
    changeValue() {
      this.$emit("changeValue", this.searchValue)
    }
  }
}
</script>

<template>
  <v-card
    v-if="$vuetify.display.mdAndUp"
    class="mx-auto"
    width="500"
    border
    rounded="pill"
    flat
  >
    <template #text>
      <div class="px-4">
        <v-text-field
          v-model="searchValue"
          density="compact"
          placeholder="Suchen"
          prepend-inner-icon="mdi-magnify"
          variant="solo"
          clearable
          flat
          hide-details
          single-line
          @input="changeValue"
        />
      </div>
    </template>
  </v-card>
  <v-card
    v-else
    class="mx-auto"
    width="300"
    border
    rounded="pill"
    flat
  >
    <template #text>
      <div class="px-4">
        <v-text-field
          v-model="searchValue"
          density="compact"
          placeholder="Suchen"
          prepend-inner-icon="mdi-magnify"
          variant="solo"
          clearable
          flat
          hide-details
          single-line
          @input="changeValue"
        />
      </div>
    </template>
  </v-card>
</template>
