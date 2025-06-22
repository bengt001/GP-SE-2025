export default interface Notification {
  id: number;
  userid: number;
  cardsDue: number;
  type: string;
  read: boolean;
}
