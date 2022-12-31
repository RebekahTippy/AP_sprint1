import Button from "@mui/material/Button";
import Box from "@mui/material/Box";
import { TextField } from "@mui/material";
import Container from "@mui/material/Container";
import React from "react";
import { Navigate } from "react-router-dom";

export default class Search extends React.Component {
  private username = "";
  private password = "";
  private login = false;

  render() {
    if (this.login) {
      return <Navigate to="/search" replace={true}></Navigate>;
    }

    return (
      <Container
        maxWidth="xs"
        sx={{ border: 1, borderColor: "primary.main", borderRadius: "16px", mt: 2 }}
      >
        <Box
          component="form"
          sx={{ "& .MuiTextField-root": { mr: 2, mb: 2, mt: 2, width: "25ch" } }}
        >
          <div>
            <h1>Access Point Class of 2022</h1>
            <span>LET'S GO GET IT!</span>

            <div>
              <TextField
                id="username"
                label="Username"
                variant="outlined"
                value={this.username}
                onChange={(e) => {
                  this.username = e.target.value;
                  this.forceUpdate();
                }}
              />
              <TextField
                label="Password"
                variant="outlined"
                type="password"
                value={this.password}
                onChange={(e) => {
                  this.password = e.target.value;
                  this.forceUpdate();
                }}
              />
            </div>
            <div>
              <Button
                onClick={(e) => {
                  if (this.password && this.username) {
                    this.login = true;
                    this.forceUpdate();
                  }
                }}
                variant="outlined"
                sx={{ mb: 2 }}
              >
                Login
              </Button>
            </div>
          </div>
        </Box>
      </Container>
    );
  }
}
