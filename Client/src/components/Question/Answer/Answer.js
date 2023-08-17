import {useState , useEffect} from 'react';
import axios from 'axios';
import AnswerData from './AnswerData';

export default function Answer() {

const [answer , setAnswer] = useState([]);

useEffect(() => {
    axios
      .get(
        `http://ec2-43-201-249-199.ap-northeast-2.compute.amazonaws.com/answers`
      )
      .then((res) => {
        setAnswer(res.data.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);
  console.log(answer);



    return (
        <>
        {answer.map((el, index) => (
            <AnswerData el={el}/>
        ))}
      
        </>
    )
}