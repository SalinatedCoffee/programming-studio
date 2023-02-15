import React, { Component, useState, useEffect } from 'react';
import { StyleSheet, Text, View, FlatList, ActivityIndicator } from 'react-native';
import { getReposByLogin } from './oauth';

export default function RepositoriesComponent({ route, navigation }) {
  const [isLoading, setLoading] = useState(true);
  const [data, setData] = useState([]);

  useEffect(() => {
    const { login } = route.params;

    getReposByLogin(login)
      .then((repos) => setData(repos))
      .catch((error) => console.error(error))
      .finally(() => setLoading(false));
  }, []);

    return (
      <View>
      {isLoading ? <ActivityIndicator size='large' color='#0000ff'/> : (
        <View style={{flexDirection:'column'}}>
            <RepositoryList repos={data}/>
        </View>
      )}
      </View>
    );
}

const RepositoryList = (props) => {
  return (
    <View style={{margin: 15, marginRight: 0}}>
      <FlatList data={props.repos} renderItem={({item}) =>
        <View style={{flexDirection: 'column', alignContent: 'flex-start', marginBottom: 15}}>
        <Text style={repositoryListStyles.repositoryNameText}>{item.nameWithOwner}</Text>
        <Text style={repositoryListStyles.repositoryDetailText} ellipsizeMode='tail' numberOfLines={3}>{item.description}</Text>
        <View style={{borderWidth: 0.6, borderColor: '#d0d0d0', marginTop:15}}/>
        </View>
      }
      keyExtractor={(item, index) => index.toString()}/>
    </View>
  );
}

const repositoryListStyles = StyleSheet.create({
    repositoryNameText: {
        color: '#000',
        fontSize: 20,
        marginBottom: 10
    },
    repositoryDetailText: {
        color: '#707070',
        fontSize: 15,
        marginRight: 15
    }
});