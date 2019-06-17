import _ from 'lodash';
import axios from 'axios';
let config = require('../../config');

let backendConfigurer = function () {
  switch (process.env.NODE_ENV) {
    case 'testing':
    case 'development':
      return 'http://' + config.dev.backendHost + ':' + config.dev.backendPort;
    case 'production':
      return 'https://' + config.build.backendHost + ':' + config.build.backendPort;
  }
}

let backendUrl = backendConfigurer();

let AXIOS = axios.create({
  baseURL: backendUrl
  // headers: {'Access-Control-Allow-Origin': frontendUrl}
});

export default {
  name: 'eventregistration',
  data() {
    return {
      persons: [],
      promoters: [],
      events: [],
      newPerson: '',
      deviceId: '',
      amount: 1,
      newPromoter: '',
      personType: 'Person',
      promoterType: 'Promoter',
      newEvent: {
        name: '',
        date: '2017-12-08',
        startTime: '09:00',
        endTime: '11:00',
        company: ''
      },
      payments: [],
      newId: '',
      newAmount: '',
      newPayment: '',
      selectedPerson: '',
      selectedPromoter: '',
      selectedPersonPay: '',
      selectedEventPay: '',
      selectedEvent: {
        name: '',
        event: '',
        deviceId: '',
        amount: ''
      },
      selectedEventPromoter: '',
      errorPerson: '',
      errorPromoter: '',
      errorEvent: '',
      errorRegistration: '',
      errorAssignation: '',
      seen: false,
      response: [],
    }
  },
  created: function () {
    // Initializing persons
    AXIOS.get('/EventRegistrationRestController/persons')
    .then(response => {
      this.persons = response.data;
      this.persons.forEach(person => this.getRegistrations(person.name))
    })
    .catch(e => {this.errorPerson = e});

    AXIOS.get('/EventRegistrationRestController/promoters')
    .then(response => {
      this.promoters = response.data;
      this.promoters.forEach(promoter => this.getAssignations(promoter.name))
    })
    .catch(e => {this.errorPromoter = e});

    AXIOS.get('/EventRegistrationRestController/events').then(response => {this.events = response.data}).catch(e => {this.errorEvent = e});

    AXIOS.get('/EventRegistrationRestController/applepay')
    .then(response => {
      this.paid = response.data;
    })
    .catch(e => {this.errorPromoter = e});
  },

  methods: {

    // createPersonType: function(event) {

    //   if(event.target.value == "Person") {
    //     // this.createPerson(event.target.value, this.personName);
    //     console.log("YOLOOOOO");
    //   }
    //   if(event.target.value == "Promoter") {
    //     // this.createPromoter(event.target.value, this.promoterName);
    //     console.log("SWAGGYP");
    //   }
    // },

    createPerson: function (personType, personName) {
      if (personType == "Person") {
        AXIOS.post('/EventRegistrationRestController/persons/'.concat(personName), {}, {})
        .then(response => {
          this.persons.push(response.data);
          this.errorPerson = '';
          this.newPerson = '';
        })
        .catch(e => {
          e = e.response.data.message ? e.response.data.message : e;
          this.errorPerson = e;
          console.log(e);
        });
      }
      if (personType == "Promoter") {
        AXIOS.post('/EventRegistrationRestController/promoters/'.concat(personName), {}, {})
        .then(response => {
          this.persons.push(response.data);
          this.promoters.push(response.data);
          this.errorPerson = '';
          this.newPerson = '';
        })
        .catch(e => {
          e = e.response.data.message ? e.response.data.message : e;
          this.errorPerson = e;
          console.log(e);
        });
      }

    },

    createEvent: function (newEvent) {
      let url = '';
      if (this.newEvent.company) {
        AXIOS.post('/EventRegistrationRestController/events/circus/'.concat(newEvent.name), {}, {params: newEvent})
        .then(response => {
          this.events.push(response.data);
          this.errorEvent = '';
          this.newEvent.name = this.newEvent.make = this.newEvent.movie = this.newEvent.company = this.newEvent.artist = this.newEvent.title = '';
        })
        .catch(e => {
          e = e.response.data.message ? e.response.data.message : e;
          this.errorEvent = e;
          console.log(e);
        });
      } else {
        AXIOS.post('/EventRegistrationRestController/events/'.concat(newEvent.name), {}, {params: newEvent})
        .then(response => {
          this.events.push(response.data);
          this.errorEvent = '';
          this.newEvent.name = this.newEvent.make = this.newEvent.movie = this.newEvent.company = this.newEvent.artist = this.newEvent.title = '';
        })
        .catch(e => {
          e = e.response.data.message ? e.response.data.message : e;
          this.errorEvent = e;
          console.log(e);
        });
      }
  },

    registerEvent: function (personName, eventName) {
      let event = this.events.find(x => x.name === eventName);
      let person = this.persons.find(x => x.name === personName);
      let params = {
        person: person.name,
        event: event.name
      };

      AXIOS.post('/EventRegistrationRestController/register', {}, {params: params})
      .then(response => {
        person.eventsAttended.push(event)
        this.selectedPerson = '';
        this.selectedEvent = '';
        this.errorRegistration = '';
      })
      .catch(e => {
        e = e.response.data.message ? e.response.data.message : e;
        this.errorRegistration = e;
        console.log(e);
      });
    },

    assignEvent: function (personName, eventName) {
      let event = this.events.find(x => x.name === eventName);
      let promoter = this.promoters.find(x => x.name === personName);
      let params = {
        promoter: promoter.name,
        event: event.name
      };

      AXIOS.post('/EventRegistrationRestController/assign', {}, {params: params})
      .then(response => {
        promoter.eventsAttended.push(event)
        this.selectedPromoter = '';
        this.selectedEventPromoter = '';
        // selectedPersonPay = '';
        // selectedEventPay = '';
        this.errorAssignation = '';
      })
      .catch(e => {
        e = e.response.data.message ? e.response.data.message : e;
        this.errorAssignation = e;
        console.log(e);
      });
    },

    payEvent: function (deviceId, amount) {
      let params = {
        amount: amount
      };

      AXIOS.post('/EventRegistrationRestController/applepay/'.concat(deviceId), {}, {params: params})
      .then(response => {
        this.errorPay = '';
      })
      .catch(e => {
        e = e.response.data.message ? e.response.data.message : e;
        this.errorPay = e;
        console.log(e);
      });
    },

    getRegistrations: function (personName) {
      AXIOS.get('/EventRegistrationRestController/events/person/'.concat(personName))
      .then(response => {
        if (!response.data || response.data.length <= 0) return;

        let indexPart = this.persons.map(x => x.name).indexOf(personName);
        this.persons[indexPart].eventsAttended = [];
        response.data.forEach(event => {
          this.persons[indexPart].eventsAttended.push(event);
        });
      })
      .catch(e => {
        e = e.response.data.message ? e.response.data.message : e;
        console.log(e);
      });
    },
  }
}
