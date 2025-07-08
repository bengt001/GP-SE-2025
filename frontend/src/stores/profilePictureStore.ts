import {ref} from 'vue'

// This is the shared, reactive timestamp
export const profilePictureTimestamp = ref(Date.now())

// Optionally, you can add a helper to update it
export function updateProfilePictureTimestamp() {
  profilePictureTimestamp.value = Date.now()
}
