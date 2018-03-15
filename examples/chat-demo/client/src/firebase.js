import firebase from 'firebase';

const config = {
    apiKey: "AIzaSyCYBMePORKmO83Opjm4Qh4GuZ1GmqMQ1ek",
    authDomain: "grails-firebase-chat-demo.firebaseapp.com",
    databaseURL: "https://grails-firebase-chat-demo.firebaseio.com",
    projectId: "grails-firebase-chat-demo",
    storageBucket: "grails-firebase-chat-demo.appspot.com",
    messagingSenderId: "307424566463"
};
firebase.initializeApp(config);

export default firebase;
