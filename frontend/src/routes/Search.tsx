import { Box, Button, TextField, List, ListItem, ListItemText, Tabs, Tab } from "@mui/material";
import Container from "@mui/material/Container";
import React from "react";

export default class Search extends React.Component {
  private selectedTab = "Topic";
  private search = "";
  private newTopic = "";
  private topics: {
    id: number;
    name: string;
  }[] = [];

  render() {
    const query = async () => {
      if (this.search.trim()) {
        this.topics = await (
          await fetch(`http://localhost:8080/api/topic/${this.search}`, {
            credentials: "omit",
          })
        ).json();
      } else {
        this.topics = [];
      }
      this.forceUpdate();
    };

    return (
      <Container sx={{ p: 2 }}>
        <Box sx={{ p: 2, border: "1px solid grey", borderRadius: "10px" }}>
          <Tabs
            value={this.selectedTab}
            onChange={(_, newTab) => {
              this.selectedTab = newTab;
              this.forceUpdate();
            }}
          >
            <Tab value="Topic" label="Topic" />
            <Tab disabled value="Messages" label="Messages" />
            <Tab disabled value="Steps" label="Steps" />
          </Tabs>
          <TextField
            id="searchTextField"
            fullWidth
            label="Keyword"
            sx={{ mt: 3, mb: 1 }}
            value={this.search}
            onChange={(e) => {
              this.search = e.target.value;
              this.forceUpdate();
            }}
          ></TextField>

          <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between" }}>
            <Button id="searchButton" disableElevation variant="contained" onClick={query}>
              Search
            </Button>
            <h3>
              {this.topics.length} result{this.topics.length === 1 ? "" : "s"}
            </h3>
          </div>

          <List>
            {this.topics.map((topic, i) => (
              <ListItem key={i}>
                <ListItemText primary={`ID: ${topic.id}`} secondary={`Topic: ${topic.name}`} />
              </ListItem>
            ))}
          </List>

          <TextField
            id="newTopicTextField"
            fullWidth
            sx={{ mt: 3, mb: 1 }}
            value={this.newTopic}
            onChange={(e) => {
              this.newTopic = e.target.value;
              this.forceUpdate();
            }}
          ></TextField>
          <Button
            id="newTopicButton"
            disableElevation
            variant="contained"
            onClick={async () => {
              if (this.newTopic.trim()) {
                await fetch("http://localhost:8080/api/topic/", {
                  method: "POST",
                  credentials: "omit",
                  headers: {
                    "Content-Type": "application/json",
                  },
                  body: JSON.stringify({
                    name: this.newTopic,
                  }),
                });
                this.newTopic = "";
                query();
              }
            }}
          >
            Add
          </Button>
        </Box>
      </Container>
    );
  }
}
