import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faMagnifyingGlass } from '@fortawesome/free-solid-svg-icons';

import { useState } from 'react';
import SearchBox from './SearchBox';

export default function Search() {

  const [isOpen, setisOpen] = useState(false)
  
  return (
    <div className='relative flex felx-row items-center px-2 mx-1 w-168 h-8 rounded-md border-2 border-solid border-gray'>
      <FontAwesomeIcon icon={faMagnifyingGlass} style={{color: "#c7c7c7"}} />
      <input className='ml-2 w-152 h-6 text-sm' placeholder="Search..." onFocus={() => {setisOpen(true)}} onBlur={() => {setisOpen(false)}}></input>
      {isOpen ? <SearchBox /> : null}
    </div>
  );
}