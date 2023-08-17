import logo from '../images/logo1.png';

export default function Footer() {
  return (
    <>
    <footer className="w-full mt-16 flex justify-around bg-[#222629] text-white">
      <img src={logo} alt="logo" className="w-[50px] h-[50px] mt-2" />
      {/* STACK OVERFLOW */}
      <div className="w-[300px] h-[400px] text-[#B0BEC5]" >
        <h3 className="mt-4 text-xl text-[#9E9E9E] ">STACK OVERFLOW.</h3>
        <p className="mt-4">Questions</p>
        <p className="mt-4">Help</p>
      </div>
      {/* PRODUCTS */}
      <div className="w-[300px] h-[350px] text-[#B0BEC5]">
        <h3 className="mt-4 text-xl text-[#9E9E9E] " >PRODUCTS</h3>
        <div className="h-[150px] mt-4 grid">
          <p>Teams</p>
          <p>Advertising</p>
          <p>Collectives</p>
          <p>Talent</p>
        </div>
      </div>
      {/* COMPANY */}
      <div className="w-[300px] h-[350px] text-[#B0BEC5]">
        <h3 className="mt-4 text-xl text-[#9E9E9E]">COMPANY</h3>
        <div className="h-[250px] mt-4 grid gap-3">
          <p>About</p>
          <p>Press</p>
          <p>Work Here</p>
          <p>Legal</p>
          <p>Privacy Policy</p>
          <p>Teams of Service</p>
          <p>Contact Us</p>
          <p>Cookie Settings</p>
          <p>Cookie Policy</p>
        </div>
      </div>
      {/* STACK EXCHANGE NETWORK */}
      <div className="w-[400px] h-[350px text-[#B0BEC5]" >
        <h3 className="mt-4 text-xl text-[#9E9E9E]">STACK EXCHANGE NETWORK</h3>
        <div className="h-[250px] mt-4 grid">
          <p>Technology</p>
          <p>Culture & Recreation</p>
          <p>Life & Arts</p>
          <p>Science</p>
          <p>Professional</p>
          <p>Business</p>
        </div>
        <div className='grid gap-3'>
            <p>API</p>
            <p>Data</p>
        </div>
      </div>
      {/* SNS ? */}
      <div className="w-[550px] h-[400px] flex flex-col justify-between text-[#B0BEC5]">
        <div className='mt-4 mr-12 flex justify-around'>
            <p>Blog</p>
            <p>FaceBook</p>
            <p>Twitter</p>
            <p>Linkedin</p>
            <p>Instagram</p>
        </div>
        <div className='mb-6 mr-12 text-xs'>
            <p>Site design / logo © 2023 Stack Exchange Inc; user contributions</p>
            <p>licensed under CC BY-SA. rev 2023.8.9.43572</p>
        </div>
      </div>
    </footer>
    </>
  );
}
