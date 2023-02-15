import React, { Component, useState, useEffect } from 'react';
import { StyleSheet, Text, View, Image, ScrollView, TouchableHighlight, ActivityIndicator } from 'react-native';
import { getUserByLogin } from './oauth';


export default function ProfileComponent({route, navigation}) {
  const [isLoading, setLoading] = useState(true);
  const [data, setData] = useState([]);

  useEffect(() => {
    const { login } = route.params;
    getUserByLogin(login)
      .then((user) => setData(user))
      .catch((error) => console.error(error))
      .finally(() => setLoading(false));
  }, []);

    return (
      <View style={{flexDirection: 'column'}}>
        {isLoading ? <ActivityIndicator size='large' color='#0000ff'/> : (
        <ScrollView style={{flexDirection: 'column'}}>
          <ProfileCluster user={data}/>
          <ContactCluster user={data}/>
          <SocialCluster user={data}/>
          <BiographyCluster user={data}/>
        </ScrollView>
        )}
      </View>
    );
}

  const ProfileCluster = (props) => {
    return (
      <View style={profileClusterStyles.container}>
          <Image style={profileClusterStyles.avatar} source={{uri: props.user.avatarUrl}} /> 
          <View style={profileClusterStyles.userNameCluster}>
            <Text style={profileClusterStyles.userNameClusterTextSupport}>@{props.user.login}</Text>
            <Text style={profileClusterStyles.userNameClusterTextMain}>{props.user.name}</Text>
            <Text style={profileClusterStyles.userNameClusterTextSupport}>Member since {props.user.createdAt}</Text>
          </View>
      </View>
    );
  }
  
  const profileClusterStyles = StyleSheet.create({
    container: {
      flexDirection: 'row',
      padding: 15
    },
    userNameCluster: {
      flexDirection: 'column',
      flex: 1,
      padding: 10
    },
    userNameClusterTextMain: {
      color: '#707070',
      fontSize: 25,
      textAlignVertical: 'center',
      flex: 1
    },
    userNameClusterTextSupport: {
      color: '#707070',
      fontSize: 15,
      flex: 0
    },
    avatar: {
      borderRadius: 100,
      height: 100,
      width: 100,
      borderColor: '#707070',
      borderWidth: 1
    }
  });
  
  const ContactCluster = (props) => {
    return (
      <View style={contactClusterStyles.container}>
        <Text style={contactClusterStyles.userContactsText}>Website: {props.user.website}</Text>
        <Text style={contactClusterStyles.userContactsText}>Email: {props.user.email}</Text>
      </View>
    );
  }
  
  const contactClusterStyles = StyleSheet.create({
    container: {
      flexDirection: 'column',
      padding: 15,
      paddingTop: 0
    },
    userContactsText: {
      color: '#707070',
      textAlignVertical: 'center',
      fontSize: 15,
      paddingBottom: 5
    }
  });
  
  const SocialCluster = (props) => {
    return (
      <View style={socialClusterStyles.container}>
        <View style={socialClusterStyles.statContainer}>
          <Text style={socialClusterStyles.statNumberText}>{props.user.numPublicRepos}</Text>
          <Text style={socialClusterStyles.statLabelText}>Repositories</Text>
        </View>
        <View style={socialClusterStyles.statContainer}>
          <Text style={socialClusterStyles.statNumberText}>{props.user.numFollowers}</Text>
          <Text style={socialClusterStyles.statLabelText}>Followers</Text>
        </View>
        <View style={socialClusterStyles.statContainer}>
          <Text style={socialClusterStyles.statNumberText}>{props.user.numFollowing}</Text>
          <Text style={socialClusterStyles.statLabelText}>Following</Text>
        </View>
      </View>
    );
  }
  
  const socialClusterStyles = StyleSheet.create({
    container: {
      flexDirection: 'row',
      padding: 15,
      paddingTop: 0
    },
    statContainer: {
      flexDirection: 'column',
      flex: 1
    },
    statNumberText: {
      color: '#707070',
      alignSelf: 'center',
      textAlign: 'center',
      fontSize: 30
    },
    statLabelText: {
      color: '#707070',
      alignSelf: 'center',
      textAlign: 'center',
      textAlignVertical: 'bottom'
    }
  });
  
  const BiographyCluster = (props) => {
    return (
      <View style={biographyClusterStyles.container}>
        <Text style={biographyClusterStyles.labelText}>Bio</Text>
        <Text style={biographyClusterStyles.text}>{props.user.bio}</Text>
      </View>
    );
  }
  
  const biographyClusterStyles = StyleSheet.create({
    container: {
      flexDirection: 'column',
      padding: 15,
      paddingTop: 0
    },
    labelText: {
      color: '#707070',
      fontSize: 15
    },
    text: {
      color: '#707070'
    }
  })
  
  class NavBar extends Component {
    _onPressButton() {
      alert('tap')
    }

    render() {
      return (
        <View style={navBarStyles.container}>
          <View style={navBarStyles.buttonContainer} onClick>
            <View style={navBarStyles.dummyImage}/>
            <Text style={navBarStyles.buttonCaptionText}>Profile</Text>
          </View>
          <TouchableHighlight underlayColor='white' onPress={this._onPressButton}>
            <View style={navBarStyles.buttonContainer}>
              <View style={navBarStyles.dummyImage}/>
              <Text style={navBarStyles.buttonCaptionText}>Repositories</Text>
            </View>
          </TouchableHighlight>
          <View style={navBarStyles.buttonContainer}>
            <View style={navBarStyles.dummyImage}/>
            <Text style={navBarStyles.buttonCaptionText}>Following</Text>
          </View>
          <View style={navBarStyles.buttonContainer}>
            <View style={navBarStyles.dummyImage}/>
            <Text style={navBarStyles.buttonCaptionText}>Followers</Text>
          </View>
        </View>
      );
    }
  }
  
  const navBarStyles = StyleSheet.create({
    container: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      backgroundColor: '#6200EE',
      paddingBottom: 7,
      paddingTop: 7
    },
    buttonContainer: {
      flexDirection: 'column',
      flex: 1
    },
    dummyImage: {
      width: 30,
      height: 30,
      alignSelf: 'center',
      backgroundColor: '#fff'
    },
    buttonCaptionText: {
      color: '#fff',
      alignSelf: 'center'
    }
  });