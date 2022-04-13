import './Profile.css';

import jwtDecode from 'jwt-decode';
import React, {useEffect, useState} from 'react';
import Blockies from 'react-blockies';

import {Token} from '../types';

interface Props {
	auth: Token;
	onLoggedOut: () => void;
}

interface State {
	loading: boolean;
	user?: {
		id: number;
		username: string;
	};
	username: string;
	data: any;
}

interface JwtDecoded {
	payload: {
		id: string;
		publicAddress: string;
	};
}

export const Profile = ({auth, onLoggedOut}: Props): JSX.Element => {
	const [state, setState] = useState<State>({
		loading: false,
		user: undefined,
		username: '',
		data: undefined
	});

	useEffect(() => {
		const {accessToken} = auth;
		const {
			payload: {id},
		} = jwtDecode<JwtDecoded>(accessToken);

		fetch(`${process.env.REACT_APP_BACKEND_URL}/user/detail`, {
			headers: {
				Authorization: `Bearer ${accessToken}`,
			},
		})
			.then((response) => response.json())
			.then((user) => setState({...state, data: user.data}))
			.catch(window.alert);
	}, []);

	const handleChange = ({
							  target: {value},
						  }: React.ChangeEvent<HTMLInputElement>) => {
		setState({...state, username: value});
	};

	const handleSubmit = () => {
		const {accessToken} = auth;
		const {data, username} = state;
		setState({...state, data, loading: true});
		if (!data) {
			window.alert(
				'The user id has not been fetched yet. Please try again in 5 seconds.'
			);
			return;
		}

		fetch(`${process.env.REACT_APP_BACKEND_URL}/user/update?name=${username}`, {
			headers: {
				Authorization: `Bearer ${accessToken}`
			},
			method: 'PATCH',
		})
			.then((response) => response.json())
			.then((user: any) => setState({
				...state,
				loading: false,
				data: user.data
			}))
			.catch((err) => {
				window.alert(err);
				setState({...state, loading: false});
			});
	};

	const {accessToken} = auth;

	const {
		payload: {publicAddress},
	} = jwtDecode<JwtDecoded>(accessToken);


	const {loading, data} = state;

	const username = data && data.username;

	return (
		<div className="Profile">
			<p>
				Logged in as <Blockies seed={publicAddress}/>
			</p>
			<div>
				My username is {username ?
				<pre>{username}</pre> : 'not set.'}{' '}
				My publicAddress is <pre>{publicAddress}</pre>
			</div>
			<div>
				<label htmlFor="username">Change username: </label>
				<input name="username" onChange={handleChange}/>
				<button disabled={loading} onClick={handleSubmit}>
					Submit
				</button>
			</div>
			<p>
				<button onClick={onLoggedOut}>Logout</button>
			</p>
		</div>
	);
};
