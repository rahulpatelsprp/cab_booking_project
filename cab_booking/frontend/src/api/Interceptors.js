import axios from "axios";
import toast from "react-hot-toast";
axios.interceptors.request.use(
    (config)=>{
        const rawToken=sessionStorage.getItem("token")
        if(rawToken)
        config.headers.Authorization=`Bearer ${rawToken}`;
        return config;
    },
    (err)=>{
        return Promise.reject(err)
    }
)
axios.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      // Token expired or invalid
      sessionStorage.clear();
      window.location.href = "/login";
    }
    if(!error.response)
        toast.error("Network error")
    else{
      toast.error(error.response.data.message)
    }
    
    return Promise.reject(error);
  }
);