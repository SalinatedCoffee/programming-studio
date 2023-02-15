import React, { Component, useState, useEffect } from 'react';
import { StyleSheet, Text, View, FlatList, ActivityIndicator, Image } from 'react-native';
import { MiniProfile } from './miniprofile';
import { getFollowersByLogin, getFollowingByLogin } from './oauth';

export default function UsersComponent({ route, navigation }) {
    const [isLoading, setLoading] = useState(true);
    const [data, setData] = useState([]);

    useEffect(() => {
        const { login, mode } = route.params;
        console.log(login, mode);
        
        if(mode === 'following') {
            console.log('Loading users in following mode.');
            getFollowingByLogin(login, 10)
                .then((users) => setData(users))
                .catch((error) => console.error(error))
                .finally(() => setLoading(false));
        }
        else if(mode === 'followers') {
            console.log('Loading users in followers mode.');
            getFollowersByLogin(login, 10)
                .then((users) => setData(users))
                .catch((error) => console.error(error))
                .finally(() => setLoading(false));
        }
    }, []);
    
        return (
          <View>
          {isLoading ? <ActivityIndicator size='large' color='#0000ff'/> : (
            <View style={{flexDirection:'column'}}>
                <UserList users={data}/>
            </View>
          )}
          </View>
        );
    }
    
    const UserList = (props) => {
      return (
        <View style={{margin: 15, marginRight: 0}}>
          <FlatList data={props.users} renderItem={({item}) =>
            <View style={{flexDirection: 'column', alignContent: 'flex-start', marginBottom: 10}}>
            <View style={{flexDirection: 'row', alignContent: 'flex-start'}}>
            <Image style={userListStyles.avatar} source={{uri: item.avatarUrl}} /> 
            <View style={{flexDirection: 'column', alignContent: 'flex-start'}}>
            <Text style={userListStyles.userLoginText}>@{item.login}</Text>
            <Text style={userListStyles.userNameText}>{item.name}</Text>
            </View>
            </View>
            <View style={{borderWidth: 0.6, borderColor: '#d0d0d0'}}/>
            </View>
          }
          keyExtractor={(item, index) => index.toString()}/>
        </View>
      );
    }
    
    const userListStyles = StyleSheet.create({
        userNameText: {
            color: '#707070',
            fontSize: 12,
            marginBottom: 15
        },
        userLoginText: {
            color: '#000',
            fontSize: 20,
            marginRight: 10
        },
        avatar: {
            width: 40,
            height: 40,
            borderRadius: 40,
            borderColor: '#707070',
            borderWidth: 1,
            marginRight: 10
        }
    });