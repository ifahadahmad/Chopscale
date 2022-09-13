import React from 'react';
import '../assets/css/navbar.css';


export const Navbar = () => {

	

	return (
		<nav className='navbar'>
			<ul>
				<a className='navBrandHref' href='/'>
					<li className='navBrand'>ChopScale</li>
				</a>
				<div className='navItem'>
					
					<a className='donateAnc' href='#'>
						<li className='donate'>Donate</li>
					</a>
					<a className='contactAnc' href='https://www.linkedin.com/in/fahad-ahmad-b042a7112/'>
						<li className='contact'>Contact</li>
					</a>
					<a href="https://github.com/ifahadahmad"><li> <i class="fab fa-github" style={{color: "rgb(31, 41, 55)"}}></i></li></a>
				</div>
			</ul>
		</nav>
	);
};
