import 'react-native-gesture-handler';
import * as React from 'react';
import { StyleSheet, Text, View, TextInput } from 'react-native';
import ProfileComponent from './ProfileComponent';
import RepositoriesComponent from './RepositoriesComponent';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import UsersComponent from './UsersComponent';

const Stack = createStackNavigator();

export default function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator screenOptions={headerStyle} initialRouteName='Home'>
        <Stack.Screen name='Home' component={HomeScreen} />
        <Stack.Screen name='Profile' component={ProfileComponent} />
        <Stack.Screen name='Repositories' component={RepositoriesComponent} />
        <Stack.Screen name='Users' component={UsersComponent} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}

const headerStyle = StyleSheet.create({
  headerStyle: {
    backgroundColor: '#6200EE'
  },
  headerTitleStyle: {
    color: 'white',
    fontWeight: '900'
  }
});

const HomeScreen = ({ navigation }) => {
  const [userlogin, setText] = React.useState('');

  return (
    <View style={{flex:1, flexDirection: 'row', justifyContent: 'space-evenly', alignItems: 'center', alignContent: 'center'}}>
    <TextInput placeholder='Enter user login. Hit enter when done.'
               onSubmitEditing={() => {
                 console.log('App: Sending login ' + userlogin.text + ' to ProfileComponent.');
                 navigation.navigate('Users', {login: userlogin.text, mode: 'followers'});
               }}
               onChangeText={(text) => {
                 setText({text});
               }}></TextInput>
    </View>
  );
}