export default interface Notification {
  id: number;
  title: string;
  messages: string[];
  type: string;
  read: boolean;
  creationDate: Date;
}
