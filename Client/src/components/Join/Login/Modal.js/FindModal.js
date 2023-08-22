import {
  findemail,
  findmodeentercode,
  findmsg,
  findinit,
} from '../../../../redux/createSlice/FindInfoSlice';
import { useDispatch, useSelector } from 'react-redux';
import FindCodeBox from './FindCodeBox';
import FindPassword from './FindPassword';
import axios from 'axios';

export default function FindModal() {
  const dispatch = useDispatch();
  const CanEnterCode = useSelector((state) => state.findinfo.mode.entercode);
  const CanEnterPW = useSelector((state) => state.findinfo.mode.enterpw);
  const FindInfo = useSelector((state) => state.findinfo);

  const handleEmail = () => {
    dispatch(findmsg(''));
    if (!FindInfo.email) {
      return dispatch(findmsg('Please enter your Email'));
    }
    return axios
      .post(
        'http://ec2-3-39-228-109.ap-northeast-2.compute.amazonaws.com/auth/email',
        { email: FindInfo.email }
      )
      .then((res) => {
        dispatch(findmodeentercode(true));
        dispatch(findmsg(`Your email has been sent successfully.`));
      })
      .catch((err) => {
        if (err.response.data.code === 400) {
          dispatch(
            findmsg(`Please enter a valid email (${err.response.data.code})`)
          );
        } else {
          dispatch(
            findmsg(`This email does not exist. (${err.response.data.code})`)
          );
        }
      });
  };

  return (
    <div className="fixed top-0 left-0 flex flex-col justify-center items-center w-screen h-screen z-20 bg-black/20">
      <div
        className="relative flex flex-col justify-center items-center w-96 mt-1 py-8 bg-white border border-solid border-gray rounded-md shadow-xss"
        onClick={(event) => {
          event.stopPropagation();
        }}
      >
        <button
          className="absolute top-2 right-4 text-2xl"
          onClick={() => dispatch(findinit())}
        >
          &times;
        </button>
        <div className="flex justify-between items-baseline w-72 text-sm">
          Forgot your account’s password?
        </div>
        <div className="my-1 w-72 text-sm">
          Enter your email address and we’ll send you a recovery link.
        </div>

        <div className="flex flex-col justify-center items-center">
          <span className="mt-2 pl-1 w-72 text-left text-md font-semibold">
            Email
          </span>
          <input
            className="my-1 w-72 h-9 pl-2 border-2 border-solid border-gray rounded-md text-sm"
            type="text"
            value={FindInfo.email}
            onChange={(e) => dispatch(findemail(e.target.value))}
          />
        </div>

        <button
          className="w-72 h-9 my-2 bg-sky-500 hover:bg-sky-400 text-sm text-white text-center rounded-md"
          onClick={handleEmail}
        >
          Send recovery email
        </button>

        {CanEnterCode && <FindCodeBox />}
        {CanEnterPW && <FindPassword />}
        <div className="my-1 text-sm text-red-500">{FindInfo.msg}</div>
      </div>
    </div>
  );
}
