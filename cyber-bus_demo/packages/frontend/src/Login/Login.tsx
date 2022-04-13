import './Login.css';

import React, {useState} from 'react';
import Web3 from 'web3';

import {Auth} from '../types';

interface Props {
	onLoggedIn: (auth: Auth) => void;
}

let web3: any; // Will hold the web3 instance

export const Login = ({onLoggedIn}: Props): JSX.Element => {
	const [loading, setLoading] = useState(false); // Loading button state

	const handleAuthenticate = ({
									publicAddress,
									signature,
									nonce
								}: {
		publicAddress: string;
		signature: string;
		nonce: string;
	}) =>
		fetch(`${process.env.REACT_APP_BACKEND_URL}/user/auth`, {
			body: JSON.stringify({
				publicAddress,
				signature,
				nonce,
				inviterCode: "x9pfRl"
			}),
			headers: {
				'Content-Type': 'application/json',
			},
			method: 'POST',
		}).then((response: any) => response.json());

	const handleSignMessage = async ({
										 publicAddress,
										 nonce,
									 }: {
		publicAddress: string;
		nonce: string;
	}) => {
		try {
			const signature = await web3.eth.personal.sign(
				`cyber-business: ${nonce}`,
				publicAddress,
				'' // MetaMask will ignore the password argument here
			);

			return {publicAddress, signature, nonce};
		} catch (err) {
			throw new Error(
				'You need to sign the message to be able to log in.'
			);
		}
	};

	const handleSignup = (publicAddress: string) =>
		fetch(`${process.env.REACT_APP_BACKEND_URL}/users`, {
			body: JSON.stringify({publicAddress}),
			headers: {
				'Content-Type': 'application/json',
			},
			method: 'POST',
		}).then((response) => response.json());

	const handleClick = async () => {
		// Check if MetaMask is installed
		if (!window.ethereum) {
			window.alert('Please install MetaMask first.');
			return;
		}

		if (!web3) {
			try {
				await window.ethereum.request<string[]>({
					method: 'eth_requestAccounts'
				})

				// We don't know window.web3 version, so we use our own instance of Web3
				// with the injected provider given by MetaMask
				web3 = new Web3(Web3.givenProvider || 'ws://some.local-or-remote.node:8546');
			} catch (error) {
				window.alert('You need to allow MetaMask.');
				return;
			}
		}

		const coinbase = await web3.eth.getCoinbase();
		if (!coinbase) {
			window.alert('Please activate MetaMask first.');
			return;
		}

		const publicAddress = coinbase.toLowerCase();
		setLoading(true);

		// Look if user with current publicAddress is already present on backend
		fetch(
			`${process.env.REACT_APP_BACKEND_URL}/user/info?publicAddress=${publicAddress}`
		)
			.then((response: any) => response.json())
			// If yes, retrieve it. If no, create it.
			/*.then((users) =>
				users.length ? users[0] : handleSignup(publicAddress)
			)*/
			// Popup MetaMask confirmation modal to sign message
			.then((res: any) => handleSignMessage({
				publicAddress: res.data.publicAddress,
				nonce: res.data.nonce
			}))
			// Send signature to backend on the /auth route
			.then(handleAuthenticate)
			// Pass accessToken back to parent component (to save it in localStorage)
			.then(onLoggedIn)
			.catch((err) => {
				window.alert(err);
				setLoading(false);
			});
	};

	return (
		<div>
			<p>
				Please select your login method.
				<br/>
				For the purpose of this demo, only MetaMask login is
				implemented.
			</p>
			<button className="Login-button Login-mm" onClick={handleClick}>
				{loading ? 'Loading...' : 'Login with MetaMask'}
			</button>
			<button className="Login-button Login-fb" disabled>
				Login with Facebook
			</button>
			<button className="Login-button Login-email" disabled>
				Login with Email
			</button>
		</div>
	);
};
